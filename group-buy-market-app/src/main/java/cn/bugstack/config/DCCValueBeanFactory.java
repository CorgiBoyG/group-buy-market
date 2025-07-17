package cn.bugstack.config;


import cn.bugstack.types.annotations.DCCValue;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.config
 * @Description: 基于 Redis 实现动态配置中心, 实现了运行时动态修改配置 的功能, 无需重启应用即可更新配置值。
 * @Author: Daniel G
 * @Create: 2025-07-17 12:39:13
 */
@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {
    //BeanPostProcessor : Spring的Bean后置处理器接口，可以在Bean初始化前后进行自定义处理

    //Redis中配置key的前缀
    private static final String BASE_CONFIG_PATH = "group_buy_market_dcc_";

    //Redis客户端，用于操作Redis
    private final RedissonClient redissonClient;

    //缓存所有包含动态配置字段的Bean对象
    private final Map<String, Object> dccObjGroup = new HashMap<>();

    public DCCValueBeanFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * B.动态监听（运行时） 运行时监听Redis消息，当配置变更时自动更新字段值
     * 监听Redis的 group_buy_market_dcc 主题
     * 通过反射动态更新Bean字段值
     * 支持AOP代理对象的处理
     */
    @Bean("dccTopic")
    public RTopic dccRedisTopicListener(RedissonClient redissonClient) {
        // 类似于订阅者Subscriber

        RTopic topic = redissonClient.getTopic("group_buy_market_dcc");
        /*处理配置变更消息*/
        topic.addListener(String.class, (charSequence, s) -> {

            /*解析消息*/
            String[] split = s.split(Constants.SPLIT);
            // 获取值
            String attribute = split[0];
            String key = BASE_CONFIG_PATH + attribute;
            String value = split[1];

            /*更新Redis*/
            RBucket<String> bucket = redissonClient.getBucket(key);
            boolean exists = bucket.isExists();
            if (!exists) return;// 配置不存在则跳过
            bucket.set(value);

            /*更新内存中的Bean字段*/
            Object objBean = dccObjGroup.get(key);
            if (null == objBean) return;

            // 处理AOP代理
            Class<?> objBeanClass = objBean.getClass();
            // 检查 objBean 是否是代理对象
            if (AopUtils.isAopProxy(objBean)) {
                // 获取代理对象的目标对象
                objBeanClass = AopUtils.getTargetClass(objBean);
            }

            try {
                // 1. getDeclaredField 方法用于获取指定类中声明的所有字段，包括私有字段、受保护字段和公共字段。
                // 2. getField 方法用于获取指定类中的公共字段，即只能获取到公共访问修饰符（public）的字段。
                // 通过反射更新字段值
                Field field = objBeanClass.getDeclaredField(attribute);
                field.setAccessible(true);
                field.set(objBean, value);
                field.setAccessible(false);

                log.info("DCC 节点监听，动态设置值 {} {}", key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return topic;
    }

    /**
     * A.Bean后置处理（启动时）
     * 启动时扫描 : Spring容器启动时， DCCValueBeanFactory 会扫描所有带有 @DCCValue 注解的字段
     * 解析配置 : 解析注解中的 value 值，提取配置键名和默认值
     * 初始化值 : 从Redis获取配置值，如果不存在则使用默认值
     * 反射注入 : 通过反射将配置值设置到对应的字段中
     * 将Bean对象缓存到 dccObjGroup 中
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 注意；Spring AOP会为Bean创建代理对象，获得类的方式要通过 AopProxyUtils.getTargetClass(bean); 不能直接 bean.getClass
        // 因为代理后类的结构发生变化，和原始对象不同，无法获得到原始的自定义注解。
        // 必须获取真实的目标对象才能读取 @DCCValue 注解

        /*处理AOP代理对象*/
        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;
        // 如果是代理对象
        if (AopUtils.isAopProxy(bean)) {
            targetBeanClass = AopUtils.getTargetClass(bean);
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
        }

        /*扫描指定类中所有字段*/
        Field[] fields = targetBeanClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(DCCValue.class)) {
                continue; // 跳过没有@DCCValue注解的字段
            }

            /*解析@DCCValue注解字段配置*/
            DCCValue dccValue = field.getAnnotation(DCCValue.class);
            String value = dccValue.value(); // 获取注解值，如"downgradeSwitch:0"
            if (StringUtils.isBlank(value)) {
                throw new RuntimeException(field.getName() + " @DCCValue is not config value config case " +
                        "「isSwitch/isSwitch:1」");
            }

            String[] splits = value.split(":");
            String key = BASE_CONFIG_PATH.concat(splits[0]); // "如group_buy_market_dcc_downgradeSwitch"
            String defaultValue = splits.length == 2 ? splits[1] : null; // "如0"

            // 设置值
            String setValue = defaultValue;
            try {
                // 如果为空则抛出异常
                if (StringUtils.isBlank(defaultValue)) {
                    throw new RuntimeException("dcc config error " + key + " is not null - 请配置默认值！");
                }

                /*Redis操作和字段赋值*/
                // Redis 操作，判断配置Key是否存在，不存在则创建，存在则获取最新值
                RBucket<String> bucket = redissonClient.getBucket(key);
                boolean exists = bucket.isExists();
                if (!exists) {
                    bucket.set(defaultValue); // Redis中不存在，设置默认值
                } else {
                    setValue = bucket.get(); // Redis中存在，获取当前值
                }

                // 通过反射设置字段值
                field.setAccessible(true);
                field.set(targetBeanObject, setValue);
                field.setAccessible(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            /*缓存Bean对象*/
            dccObjGroup.put(key, targetBeanObject);
        }

        return bean;
    }

}

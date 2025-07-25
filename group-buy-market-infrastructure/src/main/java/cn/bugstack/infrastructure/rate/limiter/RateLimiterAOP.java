package cn.bugstack.infrastructure.rate.limiter;


import cn.bugstack.types.annotations.DCCValue;
import cn.bugstack.types.annotations.RateLimiterAccessInterceptor;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.types.rate.limiter
 * @Description: 限流切面
 * @Author: Daniel G
 * @Create: 2025-07-25 17:02:21
 */
@Aspect // 标记为AOP切面类
public class RateLimiterAOP {

    private final Logger log = LoggerFactory.getLogger(RateLimiterAOP.class);

    @DCCValue("rateLimiterSwitch:open")
    private String rateLimiterSwitch; // 动态配置中心注解，获取限流开关配置【open 开启、close 关闭】关闭后，不会走限流策略

    // 个人限频记录1秒钟 Guava缓存，存储每个key对应的限流器，1秒后过期
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();

    // 个人限频黑名单24h - 分布式业务场景，可以记录到 Redis 中 黑名单缓存，存储违规次数，24小时后过期
    private final Cache<String, Long> blacklist = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    /**
     * 定义切点，拦截所有标注了@RateLimiterAccessInterceptor注解的方法。
     */
    @Pointcut("@annotation(cn.bugstack.types.annotations.RateLimiterAccessInterceptor)")
    public void aopPoint() {
    }

    /**
     * @Around 环绕通知- 可以在目标方法执行前后都执行逻辑
     * - 可以决定是否执行目标方法（ jp.proceed() ）
     */
    @Around("aopPoint() && @annotation(rateLimiterAccessInterceptor)")//应用定义的切点 和将注解实例作为参数注入方法
    public Object doRouter(ProceedingJoinPoint jp, RateLimiterAccessInterceptor rateLimiterAccessInterceptor) throws Throwable {

        // 1. 检查限流开关
        if (StringUtils.isBlank(rateLimiterSwitch) || "close".equals(rateLimiterSwitch)) {
            return jp.proceed(); // 开关关闭，直接执行原方法
        }

        // 2. 获取限流key，如"userId"
        String key = rateLimiterAccessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("annotation RateLimiter uId is null！");
        }

        // 3. 从原方法参数中提取实际的限流拦截字段 如提取userId的值 jp.getArgs()类似[LoginRequest{userId="user123", password="123456"}]
        String keyAttr = getAttrValue(key, jp.getArgs()); //如获取到user123
        log.info("aop attr {}", keyAttr);

        // 4. 黑名单拦截检查 用户第一次请求，黑名单没有这个记录
        if (!"all".equals(keyAttr) &&
                rateLimiterAccessInterceptor.blacklistCount() != 0 &&
                null != blacklist.getIfPresent(keyAttr) &&
                blacklist.getIfPresent(keyAttr) > rateLimiterAccessInterceptor.blacklistCount()) {
            log.info("限流-黑名单拦截(24h)：{}", keyAttr);
            return fallbackMethodResult(jp, rateLimiterAccessInterceptor.fallbackMethod());
        }

        // 5. 获取或创建限流 -> Guava 带缓存时间
        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttr);
        if (null == rateLimiter) {
            //创建新的RateLimiter，每秒允许*次请求
            rateLimiter = RateLimiter.create(rateLimiterAccessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttr, rateLimiter);
        }

        // 6. 限流拦截检查
        if (!rateLimiter.tryAcquire()) {// 尝试获取令牌，失败表示超过限流。 第一次请求，令牌桶有足够令牌。如超过了rateLimiter时间，则拿不到令牌
            // 更新黑名单计数
            if (rateLimiterAccessInterceptor.blacklistCount() != 0) {
                if (null == blacklist.getIfPresent(keyAttr)) {
                    blacklist.put(keyAttr, 1L); // 加1小时
                } else {
                    blacklist.put(keyAttr, blacklist.getIfPresent(keyAttr) + 1L);
                }
            }
            log.info("限流-超频次拦截：{}", keyAttr);
            return fallbackMethodResult(jp, rateLimiterAccessInterceptor.fallbackMethod());
        }

        // 7. 通过限流检查，执行原方法
        return jp.proceed();
    }

    /**
     * 调用用户配置的回调方法，当触发限流拦截后，返回回调结果。
     */
    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Signature sig = jp.getSignature();
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) sig;
        // 通过反射获取回调方法
        Method method = jp.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        // 调用回调方法并返回结果
        return method.invoke(jp.getThis(), jp.getArgs());
    }

    /**
     * 实际根据自身业务调整，主要是为了获取通过某个值做拦截 从方法参数中提取限流标识值
     */
    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            // 如果第一个参数是String，直接返回
            return args[0].toString();
        }
        String filedValue = null;
        for (Object arg : args) {
            // 遍历所有参数
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break; // 已找到值，跳出循环
                }
                // filedValue = BeanUtils.getProperty(arg, attr);
                // fix: 使用lombok时，uId这种字段的get方法与idea生成的get方法不同，会导致获取不到属性值，改成反射获取解决
                // 通过反射获取对象的指定属性值
                filedValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return filedValue;
    }

    /**
     * 获取对象的特定属性值
     *
     * @param item 对象
     * @param name 属性名
     * @return 属性值
     * @author tang
     */
    private Object getValueByName(Object item, String name) {
        try {
            Field field = getFieldByName(item, name);// 获取字段
            if (field == null) {
                return null;
            }
            field.setAccessible(true);// 设置可访问（绕过private限制）
            Object o = field.get(item);// 获取name在item中对应字段值 如user123
            field.setAccessible(false);// 恢复访问限制
            return o;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 根据名称获取方法，该方法同时兼顾继承类获取父类的属性
     *
     * @param item 对象
     * @param name 属性名
     * @return 该属性对应方法
     * @author tang
     */
    private Field getFieldByName(Object item, String name) {
        try {
            Field field;
            try {
                field = item.getClass().getDeclaredField(name);// 先从当前类获取
            } catch (NoSuchFieldException e) {
                field = item.getClass().getSuperclass().getDeclaredField(name);// 再从父类获取
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}

package cn.bugstack.types.annotations;


import java.lang.annotation.*;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.types.annotations
 * @Description: 注解，动态配置中心标记
 * @Author: Daniel G
 * @Create: 2025-07-17 11:33:02
 */

/**
 * @Retention(RetentionPolicy.RUNTIME)指定注解的生命周期 - RUNTIME : 注解在运行时仍然可用，可以通过反射获取
 * - 这是实现动态配置的关键，系统需要在运行时读取这个注解
 * <p>
 * @Target({ElementType.FIELD})指定注解可以用在哪些地方 - ElementType.FIELD : 只能用在字段（成员变量）上
 * - 限制了注解的使用范围，确保只能标记类的属性
 * <p>
 * @Documented表示这个注解会被包含在JavaDoc中 - 生成API文档时会显示这个注解
 * - 提高代码的可读性和文档完整性
 * <p>
 * String value() default "";注解的参数 - value() : 注解的主要参数，用来指定配置信息
 * - default "" : 如果不提供值，默认为空字符串
 * <p>
 * 格式 : "配置键名:默认值"
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DCCValue {

    String value() default "";

}

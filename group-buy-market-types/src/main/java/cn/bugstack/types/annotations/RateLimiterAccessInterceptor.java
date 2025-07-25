package cn.bugstack.types.annotations;

import java.lang.annotation.*;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @Description 作用 ：定义限流注解，用于标记需要限流的方法。
 * 2025-05-07 14:15
 */
@Retention(RetentionPolicy.RUNTIME)// 注解在运行时保留，可通过反射获取
@Target({ElementType.METHOD})// 注解只能用在方法上
@Documented//注解会被包含在JavaDoc中
public @interface RateLimiterAccessInterceptor {

    /**
     * 用哪个字段作为拦截标识，未配置则默认走全部
     */
    String key() default "all"; // 限流标识字段，默认"all"表示全局限流

    /**
     * 限制频次（每秒请求次数）（必填）
     */
    double permitsPerSecond();

    /**
     * 黑名单拦截（多少次限制后加入黑名单）0表示不限制
     */
    double blacklistCount() default 0;

    /**
     * 拦截后的执行方法（必填）
     */
    String fallbackMethod();

}

package cn.bugstack.config;


import cn.bugstack.infrastructure.rate.limiter.RateLimiterAOP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.config
 * @Description: Spring Boot自动配置类，自动注册限流AOP切面到Spring容器中。
 * @Author: Daniel G
 * @Create: 2025-07-25 17:06:40
 */
@Configuration
public class RateLimiterAutoConfig {

    @Bean
    // 将RateLimiterAOP注册为Spring Bean
    public RateLimiterAOP rateLimiterAOP() {
        return new RateLimiterAOP();
    }

}
/**
 * - 1.
 * 初始化 ：Spring Boot启动时自动配置RateLimiterAOP，所有标注了 @RateLimiterAccessInterceptor 的方法创建代理对象
 * - 2.
 * 方法调用 ：当调用标注了@RateLimiterAccessInterceptor的方法时
 * - 3.
 * 开关检查 ：检查限流开关是否开启
 * - 4.
 * 参数提取 ：从方法参数中提取限流标识
 * - 5.
 * 黑名单检查 ：检查是否在黑名单中
 * - 6.
 * 限流检查 ：使用Guava RateLimiter进行令牌桶限流
 * - 7.
 * 结果处理 ：通过则执行原方法，否则调用回调方法
 * 这个组件实现了基于令牌桶算法的限流功能，支持个性化限流标识、黑名单机制和动态配置。
 */

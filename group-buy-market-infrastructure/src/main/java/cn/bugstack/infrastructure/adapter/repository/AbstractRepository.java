package cn.bugstack.infrastructure.adapter.repository;


import cn.bugstack.infrastructure.dcc.DCCService;
import cn.bugstack.infrastructure.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.function.Supplier;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.adapter.repository
 * @Description: 仓储抽象类
 * @Author: Daniel G
 * @Create: 2025-07-24 23:27:48
 */

public abstract class AbstractRepository {

    private final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @Resource
    protected IRedisService redisService;

    @Resource
    protected DCCService dccService;

    /**
     * 通用缓存处理方法
     * 优先从缓存获取，缓存不存在则从数据库获取并写入缓存
     *
     * @param cacheKey   缓存键
     * @param dbFallback 数据库查询函数
     * @param <T>        返回类型
     * @return 查询结果
     * <p>
     * *
     * * <T> 是 泛型类型参数 ，表示这是一个泛型方法
     * * - Supplier<T> 是Java 8引入的 函数式接口
     * * - 定义： @FunctionalInterface public interface Supplier<T> { T get(); }
     * * - 特点：无参数，返回一个结果
     * * - 支持 延迟执行 ：只有调用 get() 方法时才执行
     * * <p>
     * * 设计模式优势
     * * 1. 策略模式 (Strategy Pattern)
     * * - Supplier<T> 作为策略接口
     * * - 不同的数据库查询逻辑作为具体策略
     * * - 缓存逻辑与业务逻辑解耦
     * * 2. 模板方法模式 (Template Method)
     * * - getFromCacheOrDb 定义了缓存处理的模板流程
     * * - 具体的数据库查询逻辑由调用方提供
     * * 3. 延迟加载 (Lazy Loading)
     * * - 只有在缓存未命中时才执行数据库查询
     * * - 提高性能，减少不必要的数据库访问
     */
    protected <T> T getFromCacheOrDb(String cacheKey, Supplier<T> dbFallback) {
        // 判断是否开启缓存
        if (dccService.isCacheOpenSwitch()) {
            // 从缓存获取
            T cacheResult = redisService.getValue(cacheKey);
            // 缓存存在则直接返回
            if (null != cacheResult) {
                return cacheResult;
            }
            // 缓存不存在则从数据库获取
            T dbResult = dbFallback.get();
            // 数据库查询结果为空则直接返回
            if (null == dbResult) {
                return null;
            }
            // 写入缓存
            redisService.setValue(cacheKey, dbResult, Duration.ofHours(1).toMillis());
            return dbResult;
        } else {
            // 缓存未开启，直接从数据库获取
            logger.warn("缓存降级 {}", cacheKey);
            return dbFallback.get();
        }
    }

    /**
     * 通用缓存处理方法（带过期时间）
     * 优先从缓存获取，缓存不存在则从数据库获取并写入缓存
     *
     * @param cacheKey   缓存键
     * @param dbFallback 数据库查询函数
     * @param expired    过期时间
     * @param <T>        返回类型
     * @return 查询结果
     */
    protected <T> T getFromCacheOrDb(String cacheKey, Supplier<T> dbFallback, long expired) {
        // 判断是否开启缓存
        if (dccService.isCacheOpenSwitch()) {
            // 从缓存获取
            T cacheResult = redisService.getValue(cacheKey);
            // 缓存存在则直接返回
            if (null != cacheResult) {
                return cacheResult;
            }
            // 缓存不存在则从数据库获取
            T dbResult = dbFallback.get();
            // 数据库查询结果为空则直接返回
            if (null == dbResult) {
                return null;
            }
            // 写入缓存（带过期时间）
            redisService.setValue(cacheKey, dbResult, expired);
            return dbResult;
        } else {
            // 缓存未开启，直接从数据库获取
            logger.warn("缓存降级 {}", cacheKey);
            return dbFallback.get();
        }
    }
}

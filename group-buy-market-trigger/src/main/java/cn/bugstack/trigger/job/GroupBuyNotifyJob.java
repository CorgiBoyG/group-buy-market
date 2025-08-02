package cn.bugstack.trigger.job;


import cn.bugstack.domain.trade.service.ITradeTaskService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.trigger.job
 * @Description: 拼团回调通知job任务补偿（加独占锁，如果实例A抢到了，实例A执行job补偿任务，其他实例等待）；拼团回调任务表，实际公司场景会定时清理数据结转，不会有太多数据挤压
 * @Author: Daniel G
 * @Create: 2025-07-18 23:15:08
 */
@Slf4j
@Component
//@Service
public class GroupBuyNotifyJob {

    @Resource
    private ITradeTaskService tradeTaskService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 本地化任务注解；@Scheduled(cron = "0 0/1 * * * ?")
     * 分布式任务注解；@XxlJob("GroupBuyNotifyJob")
     */
    @Scheduled(cron = "0 0/1 * * * ?")
//    @XxlJob("GroupBuyNotifyJob")
    public void exec() {
        // 为什么加锁？分布式应用N台机器部署互备（一个应用实例挂了，还有另外可用的），任务调度会有N个同时执行，那么这里需要增加抢占机制，谁抢占到谁就执行。完毕后，下一轮继续抢占。
        RLock lock = redissonClient.getLock("group_buy_market_notify_job_exec"); //获取一个分布式锁实例 多个线程可以获取同一个 key
        // 的锁对象，但只有一个能成功加锁
        try {
            // waitTime：等待获取锁的最长时间，快速失败机制
            // leaseTime：自动释放时间。这个时间过后，锁会自动释放。如果为0，则不自动释放锁永不过期续租时间可按照执行方法时间的耗时max来设置。如 50毫秒
            boolean isLocked = lock.tryLock(3, 0, TimeUnit.SECONDS);
            if (!isLocked) return;

            Map<String, Integer> result = tradeTaskService.execNotifyJob();
            log.info("定时任务，回调通知完成 result:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("定时任务，回调通知失败", e);
        } finally {
            // 双重检查锁状态 锁是否被持有 以及是否为当前线程持有
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}

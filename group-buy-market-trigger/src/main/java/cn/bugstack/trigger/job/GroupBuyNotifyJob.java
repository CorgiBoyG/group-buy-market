package cn.bugstack.trigger.job;


import cn.bugstack.domain.trade.service.ITradeSettlementOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.trigger.job
 * @Description: 拼团结算回调通知job任务补偿（加独占锁，如果实例A抢到了，实例A执行job补偿任务，其他实例等待）；拼团回调任务表，实际公司场景会定时清理数据结转，不会有太多数据挤压
 * @Author: Daniel G
 * @Create: 2025-07-18 23:15:08
 */
@Slf4j
@Service
public class GroupBuyNotifyJob {

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;

    @Resource
    private RedissonClient redissonClient;

    // 每 15 秒执行一次
//    @Scheduled(cron = "0/15 * * * * ?")
    // 每 2 分钟执行一次
    @Scheduled(cron = "0 0/30 * * * ?")
    public void exec() {
        // 为什么加锁？分布式应用N台机器部署互备（一个应用实例挂了，还有另外可用的），任务调度会有N个同时执行，那么这里需要增加抢占机制，谁抢占到谁就执行。完毕后，下一轮继续抢占。
        RLock lock = redissonClient.getLock("group_buy_market_notify_job_exec"); //获取一个分布式锁实例 多个线程可以获取同一个 key
        // 的锁对象，但只有一个能成功加锁
        try {
            boolean isLocked = lock.tryLock(3, 0, TimeUnit.SECONDS);//3秒锁的持有时间,3秒后自动释放锁（防止死锁）;0秒等待表示不等待,立即返回结果;快速失败机制
            if (!isLocked) return;

            Map<String, Integer> result = tradeSettlementOrderService.execSettlementNotifyJob();
            log.info("定时任务，回调通知拼团完结任务 result:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("定时任务，回调通知拼团完结任务失败", e);
        } finally {
            // 双重检查锁状态 锁是否被持有 以及是否为当前线程持有
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}

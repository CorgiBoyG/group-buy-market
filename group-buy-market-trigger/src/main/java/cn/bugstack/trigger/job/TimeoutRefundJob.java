package cn.bugstack.trigger.job;


import cn.bugstack.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundCommandEntity;
import cn.bugstack.domain.trade.service.ITradeRefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.trigger.job
 * @Description: 超时未支付订单退单定时任务
 * @Author: Daniel G
 * @Create: 2025-08-04 13:03:14
 */
@Slf4j
@Component
//@Service
public class TimeoutRefundJob {

    @Resource
    private ITradeRefundOrderService tradeRefundOrderService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 本地化任务注解；@Scheduled(cron = "0 0/15 * * * ?")
     * 分布式任务注解；@XxlJob("TimeoutRefundJob")
     */
    @Scheduled(cron = "0 0/1 * * * ?")
//    @XxlJob("TimeoutRefundJob")
    public void exec() {
        // 为什么加锁？分布式应用N台机器部署互备（一个应用实例挂了，还有另外可用的），任务调度会有N个同时执行，那么这里需要增加抢占机制，谁抢占到谁就执行。完毕后，下一轮继续抢占。
        RLock lock = redissonClient.getLock("group_buy_market_notify_job_exec"); //获取一个分布式锁实例 多个线程可以获取同一个 key
        // 的锁对象，但只有一个能成功加锁
        try {// waitTime：等待获取锁的最长时间，快速失败机制
            // leaseTime：自动释放时间。这个时间过后，锁会自动释放。如果为0，则不自动释放锁永不过期 看门狗
            boolean isLocked = lock.tryLock(3, 60, TimeUnit.SECONDS);
            if (!isLocked) {
                log.info("超时退单定时任务，获取锁失败，跳过本次执行");
                return;
            }
            log.info("超时退单定时任务开始执行");

            // 查询超时未支付订单列表
            List<UserGroupBuyOrderDetailEntity> timeoutOrderList =
                    tradeRefundOrderService.queryTimeoutUnpaidOrderList();
            if (timeoutOrderList == null || timeoutOrderList.isEmpty()) {
                log.info("超时退单定时任务，未发现超时未支付订单");
                return;
            }

            log.info("超时退单定时任务，发现超时未支付订单数量:{}", timeoutOrderList.size());
            int successCount = 0;
            int failCount = 0;
            // 遍历处理每个超时订单
            for (UserGroupBuyOrderDetailEntity orderDetail : timeoutOrderList) {
                try {
                    // 构建退单命令
                    TradeRefundCommandEntity refundCommand = TradeRefundCommandEntity.builder()
                            .userId(orderDetail.getUserId())
                            .outTradeNo(orderDetail.getOutTradeNo())
                            .source(orderDetail.getSource())
                            .channel(orderDetail.getChannel())
                            .build();

                    // 执行退单
                    tradeRefundOrderService.refundOrder(refundCommand);
                    successCount++;
                    log.info("超时订单退单成功: userId:{}, outTradeNo:{}", orderDetail.getUserId(),
                            orderDetail.getOutTradeNo());
                } catch (Exception e) {
                    failCount++;
                    log.error("超时订单退单失败: userId:{}, outTradeNo:{}, 错误信息:{}",
                            orderDetail.getUserId(), orderDetail.getOutTradeNo(), e.getMessage(), e);
                }
            }
            log.info("超时退单定时任务执行完成，成功数量:{}, 失败数量:{}", successCount, failCount);
        } catch (Exception e) {
            log.error("超时退单定时任务执行异常", e);
        } finally {
            // 双重检查锁状态 锁是否被持有 以及是否为当前线程持有
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}

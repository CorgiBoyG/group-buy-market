package cn.bugstack.infrastructure.adapter.port;


import cn.bugstack.domain.trade.adapter.port.ITradePort;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.domain.trade.model.valobj.NotifyTypeEnumVO;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.gateway.GroupBuyNotifyService;
import cn.bugstack.infrastructure.redis.IRedisService;
import cn.bugstack.types.enums.NotifyTaskHTTPEnumVO;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.adapter.port
 * @Description: Redis分布式锁 确保通知的可靠性和一致性 确保单次执行
 * @Author: Daniel G
 * @Create: 2025-07-18 22:45:25
 */
@Service
public class TradePort implements ITradePort {

    @Resource
    private GroupBuyNotifyService groupBuyNotifyService;

    @Resource
    private IRedisService redisService;

    @Resource
    private EventPublisher publisher;

    /**
     * - ✅ 防止死锁 （自动过期）
     * - ✅ 快速失败 （不阻塞等待）
     * - ✅ 安全释放 （双重检查）
     * - ✅ 异常处理 （恢复中断状态
     */
    @Override
    public String groupBuyNotify(NotifyTaskEntity notifyTaskEntity) throws Exception {
        RLock lock = redisService.getLock(notifyTaskEntity.lockKey()); //获取一个分布式锁实例 notifyTaskEntity.lockKey()
        // 多个线程可以获取同一个 key 的锁对象，但只有一个能成功加锁

        try {
            // 防重复执行: group-buy-market 拼团服务端会被部署到多台应用服务器上，那么就会有很多任务一起执行。这个时候要进行抢占，避免被多次执行
            // 锁超时设置：3秒锁的持有时间,3秒后自动释放锁（防止死锁）;0秒等待表示不等待,立即返回结果;快速失败机制
            //- lock() ：阻塞式，会一直等待直到获取锁
            //- tryLock() ：非阻塞式，立即返回结果
            // 安全释放 ：finally 块中检查锁状态并释放
            if (lock.tryLock(3, 0, TimeUnit.SECONDS)) {//尝试获取锁
                try {
                    
                    // 回调方式 HTTP
                    if (NotifyTypeEnumVO.HTTP.getCode().equals(notifyTaskEntity.getNotifyType())) {
                        // 无效的notifyUrl直接返回成功
                        if (StringUtils.isBlank(notifyTaskEntity.getNotifyUrl()) || "暂无".equals(notifyTaskEntity.getNotifyUrl())) {
                            return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                        }
                        return groupBuyNotifyService.groupBuyNotify(notifyTaskEntity.getNotifyUrl(),
                                notifyTaskEntity.getParameterJson());//执行回调
                    }

                    // 回调方式 MQ
                    if (NotifyTypeEnumVO.MQ.getCode().equals(notifyTaskEntity.getNotifyType())) {
                        publisher.publish(notifyTaskEntity.getNotifyMQ(), notifyTaskEntity.getParameterJson());
                        return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                    }

                } finally {
                    // 双重检查锁状态
                    // lock.isLocked()检查该锁 是否被任何线程持有
                    // lock.isHeldByCurrentThread() 检查该锁是否被当前线程持有
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();// 安全释放锁
                    }
                }
            }
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        } catch (Exception e) {
            Thread.currentThread().interrupt(); //中断当前线程 ,不会立即停止线程 ，只是发出中断信号
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }
    }
}

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
 * @Description: 交易接口服务（Redis分布式锁，确保多个分布式实例下，通知的可靠性和一致性，确保单次执行）
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
            // waitTime：等待获取锁的最长时间，快速失败机制
            // leaseTime：自动释放时间。这个时间过后，锁会自动释放。如果为0，则不自动释放锁永不过期续租时间可按照执行方法时间的耗时max来设置。如 50毫秒
            //- lock() ：阻塞式，会一直等待直到获取锁
            //- tryLock() ：非阻塞式，尝试获取锁，立即返回结果
            if (lock.tryLock(3, 0, TimeUnit.SECONDS)) {
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
                    // 安全释放 ：finally 块中检查锁状态并释放
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

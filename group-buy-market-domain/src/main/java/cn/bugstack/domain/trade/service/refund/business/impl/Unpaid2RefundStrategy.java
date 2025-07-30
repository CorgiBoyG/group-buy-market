package cn.bugstack.domain.trade.service.refund.business.impl;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundOrderEntity;
import cn.bugstack.domain.trade.model.valobj.TeamRefundSuccess;
import cn.bugstack.domain.trade.service.ITradeTaskService;
import cn.bugstack.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import cn.bugstack.domain.trade.service.refund.business.IRefundOrderStrategy;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.business.impl
 * @Description: 未支付，未成团；发起退单（未支付），锁单量-1、组队订单状态更新
 * @Author: Daniel G
 * @Create: 2025-07-27 10:24:15
 */
@Slf4j
@Service("unpaid2RefundStrategy")
public class Unpaid2RefundStrategy implements IRefundOrderStrategy {

    @Resource
    private ITradeRepository repository;

    @Resource
    private ITradeTaskService tradeTaskService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) {
        log.info("退单；未支付未成团 userId:{} teamId:{} orderId:{}", tradeRefundOrderEntity.getUserId(),
                tradeRefundOrderEntity.getTeamId(), tradeRefundOrderEntity.getOrderId());

        /* 1. 退单；未支付，未成团 */
        NotifyTaskEntity notifyTaskEntity =
                repository.unpaid2Refund(GroupBuyRefundAggregate.buildUnpaid2RefundAggregate(tradeRefundOrderEntity,
                        -1));

        /* 2. 发送MQ消息 - 发送MQ，恢复redis锁单库存量使用 */
        if (null != notifyTaskEntity) {
            threadPoolExecutor.execute(() -> {
                Map<String, Integer> notifyResultMap = null;
                try {
                    notifyResultMap = tradeTaskService.execNotifyJob(notifyTaskEntity);
                    log.info("回调通知交易退单（未支付未成团） result:{}", JSON.toJSONString(notifyResultMap));
                } catch (Exception e) {
                    log.error("回调通知交易退单失败（未支付未成团） result:{}", JSON.toJSONString(notifyResultMap), e);
                    throw new AppException(e.getMessage());
                }
            });
        }
    }

    @Override
    public void reverseStock(TeamRefundSuccess teamRefundSuccess) throws Exception {

        log.info("退单；恢复锁单量 - 未支付未成团，但有锁单记录，要恢复锁单库存 {} {} {}", teamRefundSuccess.getUserId(),
                teamRefundSuccess.getActivityId(), teamRefundSuccess.getTeamId());

        /* 1. 恢复库存key*/
        String recoveryTeamStockKey =
                TradeLockRuleFilterFactory.generateRecoveryTeamStockKey(teamRefundSuccess.getActivityId(),
                        teamRefundSuccess.getTeamId());
        /* 2. 退单恢复「未支付未成团，但有锁单记录，要恢复锁单库存」*/
        repository.refund2AddRecovery(recoveryTeamStockKey, teamRefundSuccess.getOrderId());
    }
}

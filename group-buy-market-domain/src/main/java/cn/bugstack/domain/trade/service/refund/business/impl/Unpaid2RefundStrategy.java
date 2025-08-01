package cn.bugstack.domain.trade.service.refund.business.impl;


import cn.bugstack.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundOrderEntity;
import cn.bugstack.domain.trade.model.valobj.TeamRefundSuccess;
import cn.bugstack.domain.trade.service.refund.business.AbstractRefundOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.business.impl
 * @Description: 未支付未成团；发起退单（未支付），锁单量-1、组队订单状态更新
 * @Author: Daniel G
 * @Create: 2025-07-27 10:24:15
 */
@Slf4j
@Service("unpaid2RefundStrategy")
public class Unpaid2RefundStrategy extends AbstractRefundOrderStrategy {

    @Override
    public void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) {
        log.info("退单；未支付未成团 userId:{} teamId:{} orderId:{}", tradeRefundOrderEntity.getUserId(),
                tradeRefundOrderEntity.getTeamId(), tradeRefundOrderEntity.getOrderId());

        /* 1. 退单；未支付，未成团 */
        NotifyTaskEntity notifyTaskEntity =
                repository.unpaid2Refund(GroupBuyRefundAggregate.buildUnpaid2RefundAggregate(tradeRefundOrderEntity,
                        -1));

        /* 2. 发送MQ消息 - 发送MQ，恢复redis锁单库存量使用 */
        sendRefundNotifyMessage(notifyTaskEntity, "未支付未成团");
    }

    @Override
    public void reverseStock(TeamRefundSuccess teamRefundSuccess) throws Exception {
        doReverseStock(teamRefundSuccess, "未支付未成团，但有锁单记录，要恢复锁单库存");
    }
}

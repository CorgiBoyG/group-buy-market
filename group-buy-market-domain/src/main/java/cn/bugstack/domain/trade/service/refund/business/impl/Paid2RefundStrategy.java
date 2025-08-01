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
 * @Description: 发起退单（已支付未成团），锁单量-1、完成量-1、组队订单状态更新、发送退单消息（MQ）
 * @Author: Daniel G
 * @Create: 2025-07-27 10:24:50
 */
@Slf4j
@Service("paid2RefundStrategy")
public class Paid2RefundStrategy extends AbstractRefundOrderStrategy {

    @Override
    public void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) throws Exception {
        log.info("退单；已支付未成团 userId:{} teamId:{} orderId:{}", tradeRefundOrderEntity.getUserId(),
                tradeRefundOrderEntity.getTeamId(), tradeRefundOrderEntity.getOrderId());

        /* 1. 退单，已支付&未成团*/
        NotifyTaskEntity notifyTaskEntity =
                repository.paid2Refund(GroupBuyRefundAggregate.buildPaid2RefundAggregate(tradeRefundOrderEntity, -1,
                        -1));

        /* 2. 发送HTTP/MQ消息，处理失败也会有定时任务补偿，通过这样异步线程池的方式，可以减轻任务调度，提高时效性*/
        sendRefundNotifyMessage(notifyTaskEntity, "已支付未成团");
    }

    @Override
    public void reverseStock(TeamRefundSuccess teamRefundSuccess) throws Exception {
        doReverseStock(teamRefundSuccess, "已支付未成团，但有锁单记录，要恢复锁单库存");

    }
}

package cn.bugstack.domain.trade.service.refund.business.impl;


import cn.bugstack.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import cn.bugstack.domain.trade.model.entity.GroupBuyTeamEntity;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundOrderEntity;
import cn.bugstack.domain.trade.model.valobj.TeamRefundSuccess;
import cn.bugstack.domain.trade.service.refund.business.AbstractRefundOrderStrategy;
import cn.bugstack.types.enums.GroupBuyOrderEnumVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.business.impl
 * @Description: 发起退单（已支付已成团），锁单量-1、完成量-1、组队订单状态更新、发送退单消息（MQ）
 * @Author: Daniel G
 * @Create: 2025-07-27 10:24:36
 */
@Slf4j
@Service("paidTeam2RefundStrategy")
public class PaidTeam2RefundStrategy extends AbstractRefundOrderStrategy {

    @Override
    public void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) {
        log.info("退单；已支付已成团 userId:{} teamId:{} orderId:{}", tradeRefundOrderEntity.getUserId(),
                tradeRefundOrderEntity.getTeamId(), tradeRefundOrderEntity.getOrderId());

        GroupBuyTeamEntity groupBuyTeamEntity =
                repository.queryGroupBuyTeamByTeamId(tradeRefundOrderEntity.getTeamId());
        Integer completeCount = groupBuyTeamEntity.getCompleteCount();

        // 最后一笔也退单，则更新拼团订单为失败
        GroupBuyOrderEnumVO groupBuyOrderEnumVO = 1 == completeCount ? GroupBuyOrderEnumVO.FAIL :
                GroupBuyOrderEnumVO.COMPLETE_FAIL;

        /* 1. 退单，已支付&已成团*/
        NotifyTaskEntity notifyTaskEntity =
                repository.paidTeam2Refund(GroupBuyRefundAggregate.buildPaidTeam2RefundAggregate(tradeRefundOrderEntity, -1, -1, groupBuyOrderEnumVO));

        /* 2. 发送MQ消息*/
        sendRefundNotifyMessage(notifyTaskEntity, "已支付已成团");

    }

    @Override
    public void reverseStock(TeamRefundSuccess teamRefundSuccess) throws Exception {
        log.info("退单；已支付已成团，队伍组队结束，不需要恢复锁单量 {} {} {}", teamRefundSuccess.getUserId(),
                teamRefundSuccess.getActivityId(), teamRefundSuccess.getTeamId());
    }
}

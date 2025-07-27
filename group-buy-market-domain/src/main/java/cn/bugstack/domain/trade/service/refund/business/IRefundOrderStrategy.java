package cn.bugstack.domain.trade.service.refund.business;


import cn.bugstack.domain.trade.model.entity.TradeRefundOrderEntity;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.business
 * @Description: 退单策略接口
 * * 未支付，Unpaid
 * * 未成团，UnformedTeam
 * * 已成团，AlreadyFormedTeam
 * @Author: Daniel G
 * @Create: 2025-07-27 10:22:27
 */
public interface IRefundOrderStrategy {

    void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) throws Exception;
}

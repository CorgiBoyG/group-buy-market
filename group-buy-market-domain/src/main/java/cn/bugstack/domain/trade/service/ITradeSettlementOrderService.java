package cn.bugstack.domain.trade.service;


import cn.bugstack.domain.trade.model.entity.TradePaySettlementEntity;
import cn.bugstack.domain.trade.model.entity.TradePaySuccessEntity;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service
 * @Description: 拼团交易结算服务接口
 * @Author: Daniel G
 * @Create: 2025-07-18 14:28:53
 */

public interface ITradeSettlementOrderService {

    /**
     * 营销结算
     *
     * @param tradePaySuccessEntity 交易支付订单实体对象
     * @return 交易结算订单实体
     */
    TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) throws Exception;
}

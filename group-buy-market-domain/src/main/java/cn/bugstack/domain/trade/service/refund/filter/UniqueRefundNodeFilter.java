package cn.bugstack.domain.trade.service.refund.filter;


import cn.bugstack.domain.trade.model.entity.MarketPayOrderEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundBehaviorEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundCommandEntity;
import cn.bugstack.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import cn.bugstack.domain.trade.service.refund.factory.TradeRefundRuleFilterFactory;
import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.filter
 * @Description: 重复退单幂等检查
 * @Author: Daniel G
 * @Create: 2025-08-01 11:43:28
 */
@Slf4j
@Service
public class UniqueRefundNodeFilter implements ILogicHandler<TradeRefundCommandEntity,
        TradeRefundRuleFilterFactory.DynamicContext, TradeRefundBehaviorEntity> {

    @Override
    public TradeRefundBehaviorEntity apply(TradeRefundCommandEntity requestParameter,
                                           TradeRefundRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("逆向流程-退单操作，重复退单检查 userId:{} outTradeNo:{}", requestParameter.getUserId(),
                requestParameter.getOutTradeNo());

        MarketPayOrderEntity marketPayOrderEntity = dynamicContext.getMarketPayOrderEntity();
        TradeOrderStatusEnumVO tradeOrderStatusEnumVO = marketPayOrderEntity.getTradeOrderStatusEnumVO();

        /* 返回幂等，已完成退单*/
        if (TradeOrderStatusEnumVO.CLOSE.equals(tradeOrderStatusEnumVO)) {
            log.info("逆向流程，退单操作(幂等-重复退单) userId:{} outTradeNo:{}", requestParameter.getUserId(),
                    requestParameter.getOutTradeNo());
            return TradeRefundBehaviorEntity.builder()
                    .userId(requestParameter.getUserId())
                    .orderId(marketPayOrderEntity.getOrderId())
                    .teamId(marketPayOrderEntity.getTeamId())
                    .tradeRefundBehaviorEnum(TradeRefundBehaviorEntity.TradeRefundBehaviorEnum.REPEAT)
                    .build();
        }

        return next(requestParameter, dynamicContext);
    }
}

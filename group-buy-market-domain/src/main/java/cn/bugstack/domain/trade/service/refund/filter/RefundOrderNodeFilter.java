package cn.bugstack.domain.trade.service.refund.filter;


import cn.bugstack.domain.trade.model.entity.*;
import cn.bugstack.domain.trade.model.valobj.RefundTypeEnumVO;
import cn.bugstack.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import cn.bugstack.domain.trade.service.refund.business.IRefundOrderStrategy;
import cn.bugstack.domain.trade.service.refund.factory.TradeRefundRuleFilterFactory;
import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;
import cn.bugstack.types.enums.GroupBuyOrderEnumVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.filter
 * @Description: 退单节点
 * @Author: Daniel G
 * @Create: 2025-08-01 11:43:34
 */
@Slf4j
@Service
public class RefundOrderNodeFilter implements ILogicHandler<TradeRefundCommandEntity,
        TradeRefundRuleFilterFactory.DynamicContext, TradeRefundBehaviorEntity> {

    @Resource
    private Map<String, IRefundOrderStrategy> refundOrderStrategyMap;

    @Override
    public TradeRefundBehaviorEntity apply(TradeRefundCommandEntity requestParameter,
                                           TradeRefundRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("逆向流程-退单操作，退单策略处理 userId:{} outTradeNo:{}", requestParameter.getUserId(),
                requestParameter.getOutTradeNo());

        // 上下文数据
        MarketPayOrderEntity marketPayOrderEntity = dynamicContext.getMarketPayOrderEntity();
        TradeOrderStatusEnumVO tradeOrderStatusEnumVO = marketPayOrderEntity.getTradeOrderStatusEnumVO();

        GroupBuyTeamEntity groupBuyTeamEntity = dynamicContext.getGroupBuyTeamEntity();
        GroupBuyOrderEnumVO groupBuyOrderEnumVO = groupBuyTeamEntity.getStatus();

        /* 3. 状态类型判断 - 使用策略模式获取退款类型*/
        RefundTypeEnumVO refundTypeEnumVO = RefundTypeEnumVO.getRefundStrategy(groupBuyOrderEnumVO,
                tradeOrderStatusEnumVO);
        IRefundOrderStrategy refundOrderStrategy = refundOrderStrategyMap.get(refundTypeEnumVO.getStrategy());

        /* 4. 执行退单*/
        refundOrderStrategy.refundOrder(TradeRefundOrderEntity.builder()
                .userId(requestParameter.getUserId())
                .orderId(marketPayOrderEntity.getOrderId())
                .activityId(groupBuyTeamEntity.getActivityId())
                .teamId(marketPayOrderEntity.getTeamId())
                .build());

        return TradeRefundBehaviorEntity.builder()
                .userId(requestParameter.getUserId())
                .orderId(marketPayOrderEntity.getOrderId())
                .teamId(marketPayOrderEntity.getTeamId())
                .tradeRefundBehaviorEnum(TradeRefundBehaviorEntity.TradeRefundBehaviorEnum.SUCCESS)
                .build();
    }
}

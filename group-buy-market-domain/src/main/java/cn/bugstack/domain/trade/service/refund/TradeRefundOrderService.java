package cn.bugstack.domain.trade.service.refund;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.entity.*;
import cn.bugstack.domain.trade.model.valobj.RefundTypeEnumVO;
import cn.bugstack.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import cn.bugstack.domain.trade.service.ITradeRefundOrderService;
import cn.bugstack.domain.trade.service.refund.business.IRefundOrderStrategy;
import cn.bugstack.types.enums.GroupBuyOrderEnumVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund
 * @Description: 退单，逆向流程服务
 * @Author: Daniel G
 * @Create: 2025-07-27 10:17:30
 */
@Slf4j
@Service
public class TradeRefundOrderService implements ITradeRefundOrderService {

    private final ITradeRepository repository;

    private final Map<String, IRefundOrderStrategy> refundOrderStrategyMap;

    public TradeRefundOrderService(ITradeRepository repository,
                                   Map<String, IRefundOrderStrategy> refundOrderStrategyMap) {
        this.repository = repository;
        this.refundOrderStrategyMap = refundOrderStrategyMap;
    }

    @Override
    public TradeRefundBehaviorEntity refundOrder(TradeRefundCommandEntity tradeRefundCommandEntity) throws Exception {

        log.info("逆向流程，退单操作 userId:{} outTradeNo:{}", tradeRefundCommandEntity.getUserId(),
                tradeRefundCommandEntity.getOutTradeNo());

        /* 1. 查询外部交易单，组队id、orderId、拼团状态*/
        MarketPayOrderEntity marketPayOrderEntity =
                repository.queryMarketPayOrderEntityByOutTradeNo(tradeRefundCommandEntity.getUserId(),
                        tradeRefundCommandEntity.getOutTradeNo());
        TradeOrderStatusEnumVO tradeOrderStatusEnumVO = marketPayOrderEntity.getTradeOrderStatusEnumVO();
        String teamId = marketPayOrderEntity.getTeamId();
        String orderId = marketPayOrderEntity.getOrderId();

        // 返回幂等，已完成退单
        if (TradeOrderStatusEnumVO.CLOSE.equals(tradeOrderStatusEnumVO)) {
            log.info("逆向流程，退单操作(幂等-重复退单) userId:{} outTradeNo:{}", tradeRefundCommandEntity.getUserId(),
                    tradeRefundCommandEntity.getOutTradeNo());
            return TradeRefundBehaviorEntity.builder()
                    .userId(tradeRefundCommandEntity.getUserId())
                    .orderId(orderId)
                    .teamId(teamId)
                    .tradeRefundBehaviorEnum(TradeRefundBehaviorEntity.TradeRefundBehaviorEnum.REPEAT)
                    .build();
        }

        /* 2. 查询拼团状态*/
        GroupBuyTeamEntity groupBuyTeamEntity = repository.queryGroupBuyTeamByTeamId(teamId);
        GroupBuyOrderEnumVO groupBuyOrderEnumVO = groupBuyTeamEntity.getStatus();

        /* 3. 状态类型判断 - 使用策略模式获取退款类型*/
        RefundTypeEnumVO refundTypeEnumVO = RefundTypeEnumVO.getRefundStrategy(groupBuyOrderEnumVO,
                tradeOrderStatusEnumVO);

        /* 4. 执行退单*/
        IRefundOrderStrategy refundOrderStrategy = refundOrderStrategyMap.get(refundTypeEnumVO.getStrategy());
        refundOrderStrategy.refundOrder(TradeRefundOrderEntity.builder()
                .userId(tradeRefundCommandEntity.getUserId())
                .orderId(orderId)
                .activityId(groupBuyTeamEntity.getActivityId())
                .teamId(teamId)
                .build());

        return TradeRefundBehaviorEntity.builder()
                .userId(tradeRefundCommandEntity.getUserId())
                .orderId(orderId)
                .teamId(teamId)
                .tradeRefundBehaviorEnum(TradeRefundBehaviorEntity.TradeRefundBehaviorEnum.SUCCESS)
                .build();
    }
}

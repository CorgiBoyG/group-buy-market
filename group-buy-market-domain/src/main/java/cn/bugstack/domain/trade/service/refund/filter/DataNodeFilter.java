package cn.bugstack.domain.trade.service.refund.filter;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.entity.GroupBuyTeamEntity;
import cn.bugstack.domain.trade.model.entity.MarketPayOrderEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundBehaviorEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundCommandEntity;
import cn.bugstack.domain.trade.service.refund.factory.TradeRefundRuleFilterFactory;
import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.filter
 * @Description: 数据节点
 * @Author: Daniel G
 * @Create: 2025-08-01 11:43:22
 */
@Slf4j
@Service
public class DataNodeFilter implements ILogicHandler<TradeRefundCommandEntity,
        TradeRefundRuleFilterFactory.DynamicContext, TradeRefundBehaviorEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeRefundBehaviorEntity apply(TradeRefundCommandEntity requestParameter,
                                           TradeRefundRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("逆向流程-退单操作，数据加载节点 userId:{} outTradeNo:{}", requestParameter.getUserId(),
                requestParameter.getOutTradeNo());

        /* 1. 查询外部交易单，组队id、orderId、拼团状态*/
        MarketPayOrderEntity marketPayOrderEntity =
                repository.queryMarketPayOrderEntityByOutTradeNo(requestParameter.getUserId(),
                        requestParameter.getOutTradeNo());
        String teamId = marketPayOrderEntity.getTeamId();

        /* 2. 查询拼团状态*/
        GroupBuyTeamEntity groupBuyTeamEntity = repository.queryGroupBuyTeamByTeamId(teamId);

        // 3. 写入上下文；如果查询数据是比较多的，可以参考 MarketNode2CompletableFuture 通过多线程进行加载
        dynamicContext.setMarketPayOrderEntity(marketPayOrderEntity);
        dynamicContext.setGroupBuyTeamEntity(groupBuyTeamEntity);

        return next(requestParameter, dynamicContext);

    }
}

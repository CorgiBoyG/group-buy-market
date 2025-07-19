package cn.bugstack.domain.trade.service.settlement.filter;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.entity.GroupBuyTeamEntity;
import cn.bugstack.domain.trade.model.entity.MarketPayOrderEntity;
import cn.bugstack.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import cn.bugstack.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import cn.bugstack.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.settlement.filter
 * @Description: 可结算规则过滤；交易时间
 * @Author: Daniel G
 * @Create: 2025-07-18 17:46:28
 */
@Slf4j
@Service
public class SettableRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity,
        TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter,
                                                     TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("结算规则过滤-有效时间校验{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // 上下文；获取数据
        MarketPayOrderEntity marketPayOrderEntity = dynamicContext.getMarketPayOrderEntity();

        // 查询组团对象
        GroupBuyTeamEntity groupBuyTeamEntity = repository.queryGroupBuyTeamByTeamId(marketPayOrderEntity.getTeamId());

        // 这里其实还得查询是否超时，超时下（超过拼团活动的有效时间）
        // 两种方案：1️⃣对结算也控制时间，2️⃣或者只控制对应参与拼团锁单时间的处理，因为有的可能结算延迟 TODO

        // 外部交易时间 - 也就是用户支付完成的时间，这个时间要在拼团有效时间范围内
        Date outTradeTime = requestParameter.getOutTradeTime();

        // 判断，外部交易时间，要小于拼团结束时间。否则抛异常。 这里外部交易时间是不是得卡在开始和结束时间范围内更好TODO
        if (!outTradeTime.before(groupBuyTeamEntity.getValidEndTime())) {
            log.error("订单交易时间不在拼团有效时间范围内");
            throw new AppException(ResponseCode.E0106);
        }

        dynamicContext.setGroupBuyTeamEntity(groupBuyTeamEntity);
        return next(requestParameter, dynamicContext);
    }
}

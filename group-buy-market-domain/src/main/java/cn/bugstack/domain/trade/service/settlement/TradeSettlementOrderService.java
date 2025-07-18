package cn.bugstack.domain.trade.service.settlement;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import cn.bugstack.domain.trade.model.entity.*;
import cn.bugstack.domain.trade.service.ITradeSettlementOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.settlement
 * @Description: 拼团交易结算服务
 * @Author: Daniel G
 * @Create: 2025-07-18 14:32:40
 */
@Slf4j
@Service
public class TradeSettlementOrderService implements ITradeSettlementOrderService {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) {

        log.info("拼团交易-支付订单结算: userId:{} outTradeNo:{}", tradePaySuccessEntity.getUserId(),
                tradePaySuccessEntity.getOutTradeNo());
        // 1. 查询拼团信息
        MarketPayOrderEntity marketPayOrderEntity =
                repository.queryMarketPayOrderEntityByOutTradeNo(tradePaySuccessEntity.getUserId(),
                        tradePaySuccessEntity.getOutTradeNo());
        if (null == marketPayOrderEntity) {
            log.info("不存在的外部交易单号或用户已退单，不需要做支付订单结算:{} outTradeNo:{}", tradePaySuccessEntity.getUserId(),
                    tradePaySuccessEntity.getOutTradeNo());
            return null;
        }

        // 这里其实还得查询是否超时，超时下（超过拼团活动的有效时间）
        // 两种方案：1️⃣对结算也控制时间，2️⃣或者只控制对应参与拼团锁单时间的处理，因为有的可能结算延迟 TODO

        // 2. 查询组团信息
        GroupBuyTeamEntity groupBuyTeamEntity = repository.queryGroupBuyTeamByTeamId(marketPayOrderEntity.getTeamId());

        // 3. 构建聚合对象
        GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate = GroupBuyTeamSettlementAggregate.builder()
                .userEntity(UserEntity.builder().userId(tradePaySuccessEntity.getUserId()).build())
                .groupBuyTeamEntity(groupBuyTeamEntity)
                .tradePaySuccessEntity(tradePaySuccessEntity)
                .build();

        // 4. 拼团交易结算
        repository.settlementMarketPayOrder(groupBuyTeamSettlementAggregate);

        // 5. 返回结算信息 - 公司中开发这样的流程时候，会根据外部需要进行值的设置
        return TradePaySettlementEntity.builder()
                .source(tradePaySuccessEntity.getSource())
                .channel(tradePaySuccessEntity.getChannel())
                .userId(tradePaySuccessEntity.getUserId())
                .teamId(marketPayOrderEntity.getTeamId())
                .activityId(groupBuyTeamEntity.getActivityId())
                .outTradeNo(tradePaySuccessEntity.getOutTradeNo())
                .build();

    }
}

package cn.bugstack.domain.trade.service.lock;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.bugstack.domain.trade.model.entity.*;
import cn.bugstack.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.bugstack.domain.trade.service.ITradeLockOrderService;
import cn.bugstack.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import cn.bugstack.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service
 * @Description: 拼团交易订单锁单服务实现类
 * @Author: Daniel G
 * @Create: 2025-07-17 17:16:12
 */
@Slf4j
@Service
public class TradeLockOrderService implements ITradeLockOrderService {

    @Resource
    private ITradeRepository repository;

    @Resource
    private BusinessLinkedList<TradeLockRuleCommandEntity, TradeLockRuleFilterFactory.DynamicContext,
            TradeLockRuleFilterBackEntity> tradeLockRuleFilter;

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        log.info("拼团交易-查询未支付营销订单:{} outTradeNo:{}", userId, outTradeNo);
        return repository.queryMarketPayOrderEntityByOutTradeNo(userId, outTradeNo);
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        log.info("拼团交易-查询拼单进度:{}", teamId);
        return repository.queryGroupBuyProgress(teamId);
    }

    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity,
                                                   PayDiscountEntity payDiscountEntity) throws Exception {
        log.info("拼团交易-锁定营销优惠支付订单: userId:{} activityId:{} goodsId:{}", userEntity.getUserId(),
                payActivityEntity.getActivityId(), payDiscountEntity.getGoodsId());

        // 交易规则过滤
        TradeLockRuleFilterBackEntity tradeLockRuleFilterBackEntity =
                tradeLockRuleFilter.apply(TradeLockRuleCommandEntity.builder()
                                .activityId(payActivityEntity.getActivityId())
                                .userId(userEntity.getUserId())
                                .teamId(payActivityEntity.getTeamId())
                                .build(),
                        new TradeLockRuleFilterFactory.DynamicContext());

        // 已参与拼团量 - 用于构建数据库唯一索引使用，确保用户只能在一个活动上参与固定的次数
        Integer userTakeOrderCount = tradeLockRuleFilterBackEntity.getUserTakeOrderCount();

        // 构建聚合对象
        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivityEntity)
                .payDiscountEntity(payDiscountEntity)
                .userTakeOrderCount(userTakeOrderCount)
                .build();

        try {
            // 锁定聚合订单 - 这会用户只是下单还没有支付。后续会有2个流程；支付成功、超时未支付（回退）
            return repository.lockMarketPayOrder(groupBuyOrderAggregate);
        } catch (Exception e) {
            // 记录失败恢复量 恢复量指的是在锁单过程中出现异常失败的时候，恢复量就会+1
            repository.recoveryTeamStock(tradeLockRuleFilterBackEntity.getRecoveryTeamStockKey(),
                    payActivityEntity.getValidTime());
            throw e;
        }

    }
}

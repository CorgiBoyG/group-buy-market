package cn.bugstack.domain.trade.service.lock.filter;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.bugstack.domain.trade.model.entity.TradeRuleCommandEntity;
import cn.bugstack.domain.trade.model.entity.TradeRuleFilterBackEntity;
import cn.bugstack.domain.trade.service.lock.factory.TradeRuleFilterFactory;
import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.filter
 * @Description: 校验用户的参与限制，规则过滤【拼团活动的参与次数】
 * @Author: Daniel G
 * @Create: 2025-07-18 11:26:04
 */
@Slf4j
@Service
public class UserTakeLimitRuleFilter implements ILogicHandler<TradeRuleCommandEntity,
        TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeRuleFilterBackEntity apply(TradeRuleCommandEntity requestParameter,
                                           TradeRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("交易规则过滤-用户参与次数校验: userId: {} activityId:{}", requestParameter.getUserId(),
                requestParameter.getActivityId());

        GroupBuyActivityEntity groupBuyActivity = dynamicContext.getGroupBuyActivity();

        // 查询用户在一个拼团活动上参与的次数
        Integer count = repository.queryOrderCountByActivityId(requestParameter.getActivityId(),
                requestParameter.getUserId());

        if (null != groupBuyActivity.getTakeLimitCount() && count >= groupBuyActivity.getTakeLimitCount()) {
            log.info("用户参与次数校验，已达可参与上限 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0103);
        }

        return TradeRuleFilterBackEntity.builder()
                .userTakeOrderCount(count)
                .build();
    }
}

package cn.bugstack.domain.activity.service.trial;


import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.types.design.framework.tree.AbstractStrategyRouter;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.trial
 * @Description: 抽象的拼团营销支撑类
 * @Author: Daniel G
 * @Create: 2025-07-15 22:09:33
 */

public abstract class AbstractGroupBuyMarketSupport<MarketProductEntity, DynamicContext, TrialBalanceEntity> extends AbstractStrategyRouter<cn.bugstack.domain.activity.model.entity.MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, cn.bugstack.domain.activity.model.entity.TrialBalanceEntity> {


}

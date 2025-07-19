package cn.bugstack.domain.activity.service;


import cn.bugstack.domain.activity.model.entity.MarketProductEntity;
import cn.bugstack.domain.activity.model.entity.TrialBalanceEntity;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service
 * @Description: 首页营销优惠服务实现类
 * @Author: Daniel G
 * @Create: 2025-07-15 22:22:51
 */
@Service
public class IndexGroupBuyMarketServiceImpl implements IIndexGroupBuyMarketService {

    @Resource
    private DefaultActivityStrategyFactory defaultActivityStrategyFactory;

    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {
        // 获取执行策略 这里返回的实际上是RootNode
        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler = defaultActivityStrategyFactory.strategyHandler();

        // 受理业务 进行优惠试算
        TrialBalanceEntity trialBalanceEntity = strategyHandler.apply(marketProductEntity,
                new DefaultActivityStrategyFactory.DynamicContext());
        return trialBalanceEntity;
    }

}

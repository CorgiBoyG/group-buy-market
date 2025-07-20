package cn.bugstack.domain.activity.service;


import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.entity.MarketProductEntity;
import cn.bugstack.domain.activity.model.entity.TrialBalanceEntity;
import cn.bugstack.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.bugstack.domain.activity.model.valobj.TeamStatisticVO;
import cn.bugstack.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private IActivityRepository repository;

    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {
        // 获取执行策略 这里返回的实际上是RootNode
        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler = defaultActivityStrategyFactory.strategyHandler();

        // 受理业务 进行优惠试算
        TrialBalanceEntity trialBalanceEntity = strategyHandler.apply(marketProductEntity,
                new DefaultActivityStrategyFactory.DynamicContext());
        return trialBalanceEntity;
    }

    @Override
    public List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailList(Long activityId,
                                                                                          String userId,
                                                                                          Integer ownerCount,
                                                                                          Integer randomCount) {
        List<UserGroupBuyOrderDetailEntity> unionAllList = new ArrayList<>();

        /* 查询某来源渠道下的商品  个人拼团数据*/
        if (0 != ownerCount) {
            List<UserGroupBuyOrderDetailEntity> ownerList =
                    repository.queryInProgressUserGroupBuyOrderDetailListByOwner(activityId, userId, ownerCount);
            if (null != ownerList && !ownerList.isEmpty()) {
                unionAllList.addAll(ownerList);
            }
        }

        /* 查询某来源渠道下的商品 当前其他非个人拼团*/
        if (0 != randomCount) {
            List<UserGroupBuyOrderDetailEntity> randomList =
                    repository.queryInProgressUserGroupBuyOrderDetailListByRandom(activityId, userId, randomCount);
            if (null != randomList && !randomList.isEmpty()) {
                unionAllList.addAll(randomList);
            }
        }

        return unionAllList;
    }

    @Override
    public TeamStatisticVO queryTeamStatisticByActivityId(Long activityId) {
        return repository.queryTeamStatisticByActivityId(activityId);
    }

}

package cn.bugstack.domain.activity.service;


import cn.bugstack.domain.activity.model.entity.MarketProductEntity;
import cn.bugstack.domain.activity.model.entity.TrialBalanceEntity;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service
 * @Description: 首页营销服务接口
 * @Author: Daniel G
 * @Create: 2025-07-15 22:20:31
 */

public interface IIndexGroupBuyMarketService {

    /**
     * 首页营销试算
     *
     * @param marketProductEntity 营销产品实体
     * @return 试算结果实体
     * @throws Exception 异常
     */
    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;

}

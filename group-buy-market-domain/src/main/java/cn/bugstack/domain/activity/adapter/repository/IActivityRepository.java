package cn.bugstack.domain.activity.adapter.repository;


import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.model.valobj.SCSkuActivityVO;
import cn.bugstack.domain.activity.model.valobj.SkuVO;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.adapter.repository
 * @Description: 活动仓储
 * @Author: Daniel G
 * @Create: 2025-07-16 09:44:44
 */

public interface IActivityRepository {

    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId);

    SkuVO querySkuByGoodsId(String goodsId);

    SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId);

    boolean isTagCrowdRange(String tagId, String userId);
}

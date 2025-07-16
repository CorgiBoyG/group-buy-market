package cn.bugstack.domain.activity.adapter.repository;


import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.model.valobj.SkuVO;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.adapter.repository
 * @Description: 活动仓储
 * @Author: Daniel G
 * @Create: 2025-07-16 09:44:44
 */

public interface IActivityRepository {

    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(String source, String channel);

    SkuVO querySkuByGoodsId(String goodsId);

}

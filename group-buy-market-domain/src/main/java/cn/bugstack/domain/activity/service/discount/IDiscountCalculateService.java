package cn.bugstack.domain.activity.service.discount;


import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.discount
 * @Description: 折扣计算服务接口
 * @Author: Daniel G
 * @Create: 2025-07-16 13:17:02
 */

public interface IDiscountCalculateService {

    /**
     * 折扣计算
     *
     * @param userId           用户ID
     * @param originalPrice    商品原始价格
     * @param groupBuyDiscount 折扣计划配置
     * @return 商品优惠价格
     */
    BigDecimal calculate(String userId, BigDecimal originalPrice,
                         GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

}

package cn.bugstack.domain.activity.service.discount;


import cn.bugstack.domain.activity.model.valobj.DiscountTypeEnum;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.discount
 * @Description: 折扣计算服务抽象类
 * @Author: Daniel G
 * @Create: 2025-07-16 13:22:46
 */

public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService {

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice,
                                GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        // 1. 人群标签过滤
        if (DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())) {
            boolean isCrowdRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if (!isCrowdRange) return originalPrice;
        }
        // 2. 折扣优惠计算
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    // 人群过滤 - 限定人群优惠
    private boolean filterTagId(String userId, String tagId) {
        // todo xiaofuge 后续开发这部分
        return true;
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice,
                                              GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

    //判断价格是否为0
    protected BigDecimal isPriceBelowZero(BigDecimal price) {
        // 判断折扣后金额，最低支付1分钱
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }
        return price;
    }
}

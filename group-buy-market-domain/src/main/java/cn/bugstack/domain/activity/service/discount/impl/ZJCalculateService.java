package cn.bugstack.domain.activity.service.discount.impl;


import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.service.discount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.discount.impl
 * @Description: 直减
 * @Author: Daniel G
 * @Create: 2025-07-16 13:34:45
 */
@Slf4j
@Service("ZJ")
public class ZJCalculateService extends AbstractDiscountCalculateService {
    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice,
                                  GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("直减优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        // 折扣表达式 - 直减为扣减金额
        String marketExpr = groupBuyDiscount.getMarketExpr();

        // 折扣价格
        BigDecimal deductionPrice = originalPrice.subtract(new BigDecimal(marketExpr));

        // 判断折扣后金额，最低支付1分钱
//        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
//            return new BigDecimal("0.01");
//        }
        deductionPrice = isPriceBelowZero(deductionPrice);

        return deductionPrice;
    }
}

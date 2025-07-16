package cn.bugstack.domain.activity.service.discount.impl;


import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.service.discount.AbstractDiscountCalculateService;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service.discount.impl
 * @Description: 每多次满减优惠计算
 * @Author: Daniel G
 * @Create: 2025-07-16 14:38:39
 */
@Slf4j
@Service("MMJ")
public class MMJCalculateService extends AbstractDiscountCalculateService {

    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice,
                                     GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("每满多少减多少优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        // 折扣表达式解析
        // 格式1: 100,10,4 每满100减10元，最多减4次
        // 格式2: 100,10,-1 每满100减10元，无次数限制
        String marketExpr = groupBuyDiscount.getMarketExpr();
        String[] split = marketExpr.split(Constants.SPLIT);

        BigDecimal x = new BigDecimal(split[0].trim()); // 满足金额
        BigDecimal y = new BigDecimal(split[1].trim()); // 减免金额

        // 解析次数限制
        int maxTimes = -1; // 默认无限制
        if (split.length >= 3) {
            maxTimes = Integer.parseInt(split[2].trim());
        }

        // 不满足最低满减约束，则按照原价
        if (originalPrice.compareTo(x) < 0) {
            return originalPrice;
        }

        // 计算满足条件的倍数
        int actualTimes = originalPrice.divide(x, 0, RoundingMode.DOWN).intValue();

        // 应用次数限制
        int finalTimes = (maxTimes > 0) ? Math.min(actualTimes, maxTimes) : actualTimes;

        // 记录次数限制日志
        if (maxTimes > 0) {
            log.info("满减次数限制: 实际满足{}次，限制{}次，最终减免{}次", actualTimes, maxTimes, finalTimes);
        }

        // 总减免金额 = 减免单价 * 最终减免次数
        BigDecimal totalDeduction = y.multiply(new BigDecimal(finalTimes));

        // 最终价格 = 原价 - 总减免金额
        BigDecimal deductionPrice = originalPrice.subtract(totalDeduction);

        // 判断折扣后金额，最低支付1分钱
//        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
//            return new BigDecimal("0.01");
//        }
        deductionPrice = isPriceBelowZero(deductionPrice);

        return deductionPrice;
    }
}

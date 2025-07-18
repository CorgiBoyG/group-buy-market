package cn.bugstack.domain.activity.model.entity;


import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.model.entity
 * @Description: 试算结果实体对象（给用户展示拼团可获得的优惠信息）
 * @Author: Daniel G
 * @Create: 2025-07-15 22:06:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrialBalanceEntity {

    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 原始价格
     */
    private BigDecimal originalPrice;
    /**
     * 折扣金额
     */
    private BigDecimal deductionPrice;
    /**
     * 最后的支付金额
     */
    private BigDecimal payPrice;
    /**
     * 拼团目标数量
     */
    private Integer targetCount;
    /**
     * 拼团开始时间
     */
    private Date startTime;
    /**
     * 拼团结束时间
     */
    private Date endTime;
    /**
     * 是否可见拼团
     */
    private Boolean isVisible;
    /**
     * 是否可参与进团
     */
    private Boolean isEnable;
    /**
     * 活动配置信息
     */
    private GroupBuyActivityDiscountVO groupBuyActivityDiscountVO;

}

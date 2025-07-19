package cn.bugstack.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model
 * @Description: 拼团，支付优惠实体对象
 * @Author: Daniel G
 * @Create: 2025-07-17 16:54:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayDiscountEntity {

    /**
     * 渠道
     */
    private String source;
    /**
     * 来源
     */
    private String channel;
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
     * 外部交易单号-确保外部调用唯一幂等
     */
    private String outTradeNo;
    /**
     * 回调通知地址
     */
    private String notifyUrl;
}

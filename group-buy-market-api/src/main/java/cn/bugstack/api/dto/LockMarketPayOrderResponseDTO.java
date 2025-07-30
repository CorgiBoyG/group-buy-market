package cn.bugstack.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api.dto
 * @Description: 营销支付锁单应答对象
 * @Author: Daniel G
 * @Create: 2025-07-17 18:07:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockMarketPayOrderResponseDTO {

    /**
     * 预购订单ID
     */
    private String orderId;
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
     * 交易订单状态
     */
    private Integer tradeOrderStatus;
    /**
     * 组队ID
     */
    private String teamId;

}

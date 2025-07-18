package cn.bugstack.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.entity
 * @Description: 交易结算订单实体
 * @Author: Daniel G
 * @Create: 2025-07-18 14:31:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePaySettlementEntity {

    /**
     * 渠道
     */
    private String source;
    /**
     * 来源
     */
    private String channel;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 拼单组队ID
     */
    private String teamId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 外部交易单号
     */
    private String outTradeNo;

}
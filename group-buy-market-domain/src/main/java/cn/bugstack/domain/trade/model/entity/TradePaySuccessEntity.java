package cn.bugstack.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.entity
 * @Description: 交易支付订单实体对象
 * @Author: Daniel G
 * @Create: 2025-07-18 14:29:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePaySuccessEntity {

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
     * 外部交易单号
     */
    private String outTradeNo;

}

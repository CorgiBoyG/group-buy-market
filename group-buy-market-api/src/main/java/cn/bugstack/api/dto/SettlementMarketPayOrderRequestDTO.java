package cn.bugstack.api.dto;


import lombok.Data;

import java.util.Date;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api.dto
 * @Description: 结算请求对象
 * @Author: Daniel G
 * @Create: 2025-07-20 18:23:11
 */
@Data
public class SettlementMarketPayOrderRequestDTO {

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
    /**
     * 外部交易时间
     */
    private Date outTradeTime;


}

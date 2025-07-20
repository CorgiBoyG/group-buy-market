package cn.bugstack.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api.dto
 * @Description: 结算应答对象
 * @Author: Daniel G
 * @Create: 2025-07-20 18:23:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementMarketPayOrderResponseDTO {
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

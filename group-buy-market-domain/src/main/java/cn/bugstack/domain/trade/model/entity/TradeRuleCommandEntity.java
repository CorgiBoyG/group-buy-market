package cn.bugstack.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.entity
 * @Description: 拼团交易命令实体
 * @Author: Daniel G
 * @Create: 2025-07-18 11:21:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeRuleCommandEntity {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;

}

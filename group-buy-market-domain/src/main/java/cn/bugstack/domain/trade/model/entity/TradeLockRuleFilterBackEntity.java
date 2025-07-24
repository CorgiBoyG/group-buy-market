package cn.bugstack.domain.trade.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.entity
 * @Description: 拼团交易锁单规则责任链命令
 * @Author: Daniel G
 * @Create: 2025-07-18 11:21:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeLockRuleFilterBackEntity {

    // 用户参与活动的订单量
    private Integer userTakeOrderCount;

    // 恢复组队库存缓存key
    private String recoveryTeamStockKey;
}

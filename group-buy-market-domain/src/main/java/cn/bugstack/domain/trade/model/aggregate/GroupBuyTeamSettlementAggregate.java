package cn.bugstack.domain.trade.model.aggregate;


import cn.bugstack.domain.trade.model.entity.GroupBuyTeamEntity;
import cn.bugstack.domain.trade.model.entity.TradePaySuccessEntity;
import cn.bugstack.domain.trade.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.aggregate
 * @Description: 拼团组队结算聚合
 * @Author: Daniel G
 * @Create: 2025-07-18 14:49:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTeamSettlementAggregate {

    /**
     * 用户实体对象
     */
    private UserEntity userEntity;
    /**
     * 拼团组队实体对象
     */
    private GroupBuyTeamEntity groupBuyTeamEntity;
    /**
     * 交易支付订单实体对象
     */
    private TradePaySuccessEntity tradePaySuccessEntity;

}

package cn.bugstack.domain.trade.model.aggregate;


import cn.bugstack.domain.trade.model.entity.TradeRefundOrderEntity;
import cn.bugstack.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.bugstack.types.enums.GroupBuyOrderEnumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.aggregate
 * @Description: 拼团退单聚合
 * @Author: Daniel G
 * @Create: 2025-07-27 10:32:56
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyRefundAggregate {

    /**
     * 交易退单
     */
    private TradeRefundOrderEntity tradeRefundOrderEntity;

    /**
     * 退单进度
     */
    private GroupBuyProgressVO groupBuyProgress;

    /**
     * 拼团枚举
     */
    private GroupBuyOrderEnumVO groupBuyOrderEnumVO;


    /**
     * 未支付,未成团
     */
    public static GroupBuyRefundAggregate buildUnpaid2RefundAggregate(TradeRefundOrderEntity tradeRefundOrderEntity,
                                                                      Integer lockCount) {
        GroupBuyRefundAggregate groupBuyRefundAggregate = new GroupBuyRefundAggregate();
        groupBuyRefundAggregate.setTradeRefundOrderEntity(tradeRefundOrderEntity);
        groupBuyRefundAggregate.setGroupBuyProgress(
                GroupBuyProgressVO.builder()
                        .lockCount(lockCount)
                        .build());
        return groupBuyRefundAggregate;
    }

    /**
     * 已支付 未成团
     */
    public static GroupBuyRefundAggregate buildPaid2RefundAggregate(TradeRefundOrderEntity tradeRefundOrderEntity,
                                                                    Integer lockCount,
                                                                    Integer completeCount) {
        GroupBuyRefundAggregate groupBuyRefundAggregate = new GroupBuyRefundAggregate();
        groupBuyRefundAggregate.setTradeRefundOrderEntity(tradeRefundOrderEntity);
        groupBuyRefundAggregate.setGroupBuyProgress(
                GroupBuyProgressVO.builder()
                        .lockCount(lockCount)
                        .completeCount(completeCount)
                        .build());

        return groupBuyRefundAggregate;
    }

    /**
     * 已支付 已成团
     */
    public static GroupBuyRefundAggregate buildPaidTeam2RefundAggregate(TradeRefundOrderEntity tradeRefundOrderEntity,
                                                                        Integer lockCount,
                                                                        Integer completeCount,
                                                                        GroupBuyOrderEnumVO groupBuyOrderEnumVO) {
        GroupBuyRefundAggregate groupBuyRefundAggregate = new GroupBuyRefundAggregate();
        groupBuyRefundAggregate.setTradeRefundOrderEntity(tradeRefundOrderEntity);
        groupBuyRefundAggregate.setGroupBuyProgress(
                GroupBuyProgressVO.builder()
                        .lockCount(lockCount)
                        .completeCount(completeCount)
                        .build());
        groupBuyRefundAggregate.setGroupBuyOrderEnumVO(groupBuyOrderEnumVO);

        return groupBuyRefundAggregate;
    }
}

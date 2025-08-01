package cn.bugstack.domain.trade.service.refund.factory;


import cn.bugstack.domain.trade.model.entity.GroupBuyTeamEntity;
import cn.bugstack.domain.trade.model.entity.MarketPayOrderEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundBehaviorEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundCommandEntity;
import cn.bugstack.domain.trade.service.refund.filter.DataNodeFilter;
import cn.bugstack.domain.trade.service.refund.filter.RefundOrderNodeFilter;
import cn.bugstack.domain.trade.service.refund.filter.UniqueRefundNodeFilter;
import cn.bugstack.types.design.framework.link.model2.LinkArmory;
import cn.bugstack.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.factory
 * @Description: 交易退单节点工厂
 * @Author: Daniel G
 * @Create: 2025-08-01 11:42:52
 */
@Slf4j
@Service
public class TradeRefundRuleFilterFactory {

    @Bean("tradeRefundRuleFilter")
    public BusinessLinkedList<TradeRefundCommandEntity, TradeRefundRuleFilterFactory.DynamicContext,
            TradeRefundBehaviorEntity> tradeRefundRuleFilter(DataNodeFilter dataNodeFilter,
                                                             UniqueRefundNodeFilter uniqueRefundNodeFilter,
                                                             RefundOrderNodeFilter refundOrderNodeFilter) {
        // 组装链
        LinkArmory<TradeRefundCommandEntity, TradeRefundRuleFilterFactory.DynamicContext,
                TradeRefundBehaviorEntity> linkArmory =
                new LinkArmory<>("退单规则过滤链", dataNodeFilter, uniqueRefundNodeFilter, refundOrderNodeFilter);

        // 链对象
        return linkArmory.getLogicLink();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        /*外部交易单*/
        private MarketPayOrderEntity marketPayOrderEntity;
        /*拼团状态*/
        private GroupBuyTeamEntity groupBuyTeamEntity;

    }
}

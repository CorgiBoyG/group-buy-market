package cn.bugstack.domain.trade.service.lock.factory;


import cn.bugstack.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.bugstack.domain.trade.model.entity.TradeLockRuleCommandEntity;
import cn.bugstack.domain.trade.model.entity.TradeLockRuleFilterBackEntity;
import cn.bugstack.domain.trade.service.lock.filter.ActivityUsabilityRuleFilter;
import cn.bugstack.domain.trade.service.lock.filter.TeamStockOccupyRuleFilter;
import cn.bugstack.domain.trade.service.lock.filter.UserTakeLimitRuleFilter;
import cn.bugstack.types.design.framework.link.model2.LinkArmory;
import cn.bugstack.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.factory
 * @Description: 交易锁单规则链过滤工厂
 * @Author: Daniel G
 * @Create: 2025-07-18 11:23:06
 */
@Slf4j
@Service
public class TradeLockRuleFilterFactory {

    @Bean("tradeLockRuleFilter")
    public BusinessLinkedList<TradeLockRuleCommandEntity, TradeLockRuleFilterFactory.DynamicContext,
            TradeLockRuleFilterBackEntity> tradeLockRuleFilter(ActivityUsabilityRuleFilter activityUsabilityRuleFilter,
                                                               UserTakeLimitRuleFilter userTakeLimitRuleFilter,
                                                               TeamStockOccupyRuleFilter teamStockOccupyRuleFilter) {
        // 组装链
        LinkArmory<TradeLockRuleCommandEntity, TradeLockRuleFilterFactory.DynamicContext,
                TradeLockRuleFilterBackEntity> linkArmory =
                new LinkArmory<>("交易规则过滤链", activityUsabilityRuleFilter, userTakeLimitRuleFilter,
                        teamStockOccupyRuleFilter);

        // 链对象
        return linkArmory.getLogicLink();
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        // 锁
        private String teamStockKey = "group_buy_market_team_stock_key_";
        // 拼团活动实体对象
        private GroupBuyActivityEntity groupBuyActivity;
        // 当前用户参与该拼团活动的次数
        private Integer userTakeOrderCount;

        public String generateTeamStockKey(String teamId) {
            if (StringUtils.isBlank(teamId)) return null;
            return teamStockKey + groupBuyActivity.getActivityId() + "_" + teamId;
        }

        public String generateRecoveryTeamStockKey(String teamId) {
            if (StringUtils.isBlank(teamId)) return null;
            return teamStockKey + groupBuyActivity.getActivityId() + "_" + teamId + "_recovery";
        }
    }

}

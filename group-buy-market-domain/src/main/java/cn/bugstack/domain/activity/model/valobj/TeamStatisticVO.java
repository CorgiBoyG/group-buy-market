package cn.bugstack.domain.activity.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.model.valobj
 * @Description: 队伍统计值对象
 * @Author: Daniel G
 * @Create: 2025-07-20 21:07:06
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamStatisticVO {

    /**
     * 开团队伍数量
     */
    private Integer allTeamCount;
    /**
     * 成团队伍数量
     */
    private Integer allTeamCompleteCount;
    /**
     * 参团人数总量 - 一个商品的总参团人数 计算的是lockCount
     */
    private Integer allTeamUserCount;
}

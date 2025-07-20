package cn.bugstack.domain.activity.service;


import cn.bugstack.domain.activity.model.entity.MarketProductEntity;
import cn.bugstack.domain.activity.model.entity.TrialBalanceEntity;
import cn.bugstack.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.bugstack.domain.activity.model.valobj.TeamStatisticVO;

import java.util.List;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.service
 * @Description: 首页营销服务接口
 * @Author: Daniel G
 * @Create: 2025-07-15 22:20:31
 */

public interface IIndexGroupBuyMarketService {

    /**
     * 首页营销试算
     *
     * @param marketProductEntity 营销产品实体
     * @return 试算结果实体
     * @throws Exception 异常
     */
    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;

    /**
     * 查询进行中的拼团订单
     *
     * @param activityId  活动ID
     * @param userId      用户ID
     * @param ownerCount  个人数量
     * @param randomCount 随机数量
     * @return 用户拼团明细数据
     */
    List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailList(Long activityId, String userId,
                                                                                   Integer ownerCount,
                                                                                   Integer randomCount);

    /**
     * 活动拼团队伍总结
     *
     * @param activityId 活动ID
     * @return 队伍统计
     */
    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);
}

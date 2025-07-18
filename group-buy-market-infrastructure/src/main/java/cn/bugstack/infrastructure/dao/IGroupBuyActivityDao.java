package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.GroupBuyActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 拼团活动Dao
 * @create 2024-12-07 10:10
 */
@Mapper
public interface IGroupBuyActivityDao {

    /**
     * @return 查询所有的拼团活动GroupBuyActivity list
     */
    List<GroupBuyActivity> queryGroupBuyActivityList();

    /**
     * @param groupBuyActivityReq:GroupBuyActivity 封装了商品来源、商品渠道
     * @return 根据来源、渠道查询拼团活动GroupBuyActivity
     */
    GroupBuyActivity queryValidGroupBuyActivity(GroupBuyActivity groupBuyActivityReq);

    /**
     * @param activityId
     * @return 根据activityId 查询拼团活动GroupBuyActivity
     */
    GroupBuyActivity queryValidGroupBuyActivityId(Long activityId);

    GroupBuyActivity queryGroupBuyActivityByActivityId(Long activityId);
}

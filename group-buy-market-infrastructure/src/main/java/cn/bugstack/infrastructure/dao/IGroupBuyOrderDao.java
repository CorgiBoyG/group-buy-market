package cn.bugstack.infrastructure.dao;


import cn.bugstack.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.dao
 * @Description: 用户拼单Dao
 * @Author: Daniel G
 * @Create: 2025-07-17 16:40:36
 */
@Mapper
public interface IGroupBuyOrderDao {

    /**
     * 插入拼团单记录
     *
     * @param groupBuyOrder 拼单订单对象
     */
    void insert(GroupBuyOrder groupBuyOrder);

    /**
     * 更新增加锁单数量
     *
     * @param teamId 拼团组ID
     * @return 更新记录数
     */
    int updateAddLockCount(String teamId);

    /**
     * 更新减少锁单数量
     *
     * @param teamId 拼团组ID
     * @return 更新记录数
     */
    int updateSubtractionLockCount(String teamId);

    /**
     * 查询拼单进度
     *
     * @param teamId 拼团组ID
     * @return 拼单订单对象
     */
    GroupBuyOrder queryGroupBuyProgress(String teamId);

    /**
     * 根据拼团组ID查询拼团订单
     *
     * @param teamId 拼团组ID
     * @return 拼团订单对象
     */
    GroupBuyOrder queryGroupBuyTeamByTeamId(String teamId);

    /**
     * 更新增加完成数量
     *
     * @param teamId 拼团组ID
     * @return 更新记录数
     */
    int updateAddCompleteCount(String teamId);

    /**
     * 更新拼团订单状态为完成
     *
     * @param teamId 拼团组ID
     * @return 更新记录数
     */
    int updateOrderStatus2COMPLETE(String teamId);

    /**
     * 根据拼团组ID集合查询还没有完成的拼团
     *
     * @param teamIds 拼团组ID集合
     * @return 拼团订单列表
     */
    List<GroupBuyOrder> queryGroupBuyProgressByTeamIds(@Param("teamIds") Set<String> teamIds);

    /**
     * 查询指定拼团组的总团队数量
     *
     * @param teamIds 拼团组ID集合
     * @return 团队总数量
     */
    Integer queryAllTeamCount(@Param("teamIds") Set<String> teamIds);

    /**
     * 查询指定拼团组中已完成的团队数量
     *
     * @param teamIds 拼团组ID集合
     * @return 已完成团队数量
     */
    Integer queryAllTeamCompleteCount(@Param("teamIds") Set<String> teamIds);

    /**
     * 查询指定拼团组的总用户参与数量
     *
     * @param teamIds 拼团组ID集合
     * @return 用户总数量
     */
    Integer queryAllUserCount(@Param("teamIds") Set<String> teamIds);

    int unpaid2Refund(GroupBuyOrder groupBuyOrderReq);

    int paid2Refund(GroupBuyOrder groupBuyOrderReq);

    int paidTeam2Refund(GroupBuyOrder groupBuyOrderReq);

    int paidTeam2RefundFail(GroupBuyOrder groupBuyOrderReq);
}

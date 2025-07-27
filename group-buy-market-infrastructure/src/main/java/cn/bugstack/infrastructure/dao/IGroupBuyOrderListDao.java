package cn.bugstack.infrastructure.dao;


import cn.bugstack.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.dao
 * @Description: 用户拼单明细Dao
 * @Author: Daniel G
 * @Create: 2025-07-17 16:41:05
 */
@Mapper
public interface IGroupBuyOrderListDao {

    /**
     * 插入拼单明细记录
     *
     * @param groupBuyOrderListReq 拼单明细对象
     */
    void insert(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 根据外部交易号和用户Id 查询拼单订单记录
     *
     * @param groupBuyOrderListReq 拼单明细查询对象
     * @return 拼单明细对象
     */
    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(GroupBuyOrderList groupBuyOrderListReq);


    /**
     * 根据活动ID和用户ID 查询订单数量
     *
     * @param groupBuyOrderListReq 拼单明细查询对象
     * @return 订单数量
     */
    Integer queryOrderCountByActivityId(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 根据外部交易单号和用户ID 更新订单状态为完成
     *
     * @param groupBuyOrderListReq 拼单明细对象
     * @return 更新记录数
     */
    int updateOrderStatus2COMPLETE(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 根据拼团组ID查询已完成订单的外部交易号列表
     *
     * @param teamId 拼团组ID
     * @return 外部交易号列表
     */
    List<String> queryGroupBuyCompleteOrderOutTradeNoListByTeamId(String teamId);

    /**
     * 根据用户ID和活动ID 查询进行中的拼团订单明细列表
     *
     * @param groupBuyOrderListReq 拼单明细查询对象
     * @return 进行中的拼团订单明细列表
     */
    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByUserId(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 根据用户ID和活动ID 随机查询进行中的非个人拼团订单明细列表
     *
     * @param groupBuyOrderListReq 拼单明细查询对象
     * @return 进行中的拼团订单明细列表
     */
    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByRandom(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 根据活动ID 查询进行中的拼团订单明细列表 唯一的拼团组列表
     * 取消了group by 要不然就更改sql模式 SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES,
     * NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
     *
     * @param activityId 活动ID
     * @return 进行中的拼团订单明细列表
     */
    List<GroupBuyOrderList> queryInProgressUserGroupBuyOrderDetailListByActivityId(Long activityId);

    int unpaid2Refund(GroupBuyOrderList groupBuyOrderListReq);

    int paid2Refund(GroupBuyOrderList groupBuyOrderListReq);

    int paidTeam2Refund(GroupBuyOrderList groupBuyOrderListReq);
}

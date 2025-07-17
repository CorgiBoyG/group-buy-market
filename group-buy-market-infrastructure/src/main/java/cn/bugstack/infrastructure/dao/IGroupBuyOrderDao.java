package cn.bugstack.infrastructure.dao;


import cn.bugstack.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;

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
}

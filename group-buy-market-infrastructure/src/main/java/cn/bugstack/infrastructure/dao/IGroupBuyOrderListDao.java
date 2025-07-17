package cn.bugstack.infrastructure.dao;


import cn.bugstack.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

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


}

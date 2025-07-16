package cn.bugstack.infrastructure.dao;

import cn.bugstack.infrastructure.dao.po.GroupBuyDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 折扣配置Dao
 * @create 2024-12-07 10:10
 */
@Mapper
public interface IGroupBuyDiscountDao {

    /**
     * @return 查询所有折扣配置list
     */
    List<GroupBuyDiscount> queryGroupBuyDiscountList();

    /**
     * @return 根据折扣id查询折扣配置
     */
    GroupBuyDiscount queryGroupBuyActivityDiscountByDiscountId(String discountId);

}

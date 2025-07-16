package cn.bugstack.infrastructure.dao;


import cn.bugstack.infrastructure.dao.po.SCSkuActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.dao
 * @Description: 渠道商品活动配置关联表Dao
 * @Author: Daniel G
 * @Create: 2025-07-16 19:06:41
 */

@Mapper
public interface ISCSkuActivityDao {

    /**
     * @param scSkuActivity:SCSkuActivity 封装了商品的source、channel、goodsId
     * @return 根据source、channel、goodsId 查询渠道商品活动配置【实际上就用了goodsId】
     */
    SCSkuActivity querySCSkuActivityBySCGoodsId(SCSkuActivity scSkuActivity);
}

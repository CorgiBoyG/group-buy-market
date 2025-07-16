package cn.bugstack.infrastructure.dao;


import cn.bugstack.infrastructure.dao.po.Sku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.dao
 * @Description: 商品查询
 * @Author: Daniel G
 * @Create: 2025-07-16 09:57:32
 */
@Mapper
public interface ISkuDao {

    /**
     * @param goodsId 商品id
     * @return 根据商品id查询商品信息
     */
    Sku querySkuByGoodsId(String goodsId);
}

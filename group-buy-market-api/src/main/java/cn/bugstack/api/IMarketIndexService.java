package cn.bugstack.api;


import cn.bugstack.api.dto.GoodsMarketRequestDTO;
import cn.bugstack.api.dto.GoodsMarketResponseDTO;
import cn.bugstack.api.response.Response;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api
 * @Description: 营销首页服务接口
 * @Author: Daniel G
 * @Create: 2025-07-20 18:36:47
 */

public interface IMarketIndexService {

    /**
     * 查询拼团营销配置
     *
     * @param goodsMarketRequestDTO 营销商品信息
     * @return 营销配置信息
     */
    Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(GoodsMarketRequestDTO goodsMarketRequestDTO);
}

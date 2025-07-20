package cn.bugstack.api.dto;


import lombok.Data;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.api.dto
 * @Description: 商品营销请求对象
 * @Author: Daniel G
 * @Create: 2025-07-20 18:37:56
 */
@Data
public class GoodsMarketRequestDTO {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 渠道
     */
    private String source;
    /**
     * 来源
     */
    private String channel;
    /**
     * 商品ID
     */
    private String goodsId;
}

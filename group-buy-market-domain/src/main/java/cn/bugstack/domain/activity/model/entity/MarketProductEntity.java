package cn.bugstack.domain.activity.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.model.entity
 * @Description: 营销商品实体信息，通过这样一个信息获取商品优惠信息
 * @Author: Daniel G
 * @Create: 2025-07-15 22:05:37
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketProductEntity {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 渠道
     */
    private String source;
    /**
     * 来源
     */
    private String channel;
}

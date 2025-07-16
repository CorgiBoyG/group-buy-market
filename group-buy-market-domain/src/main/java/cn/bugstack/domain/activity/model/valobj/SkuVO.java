package cn.bugstack.domain.activity.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.activity.model.valobj
 * @Description: 商品信息对象
 * @Author: Daniel G
 * @Create: 2025-07-16 10:08:10
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuVO {

    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 原始价格
     */
    private BigDecimal originalPrice;

}

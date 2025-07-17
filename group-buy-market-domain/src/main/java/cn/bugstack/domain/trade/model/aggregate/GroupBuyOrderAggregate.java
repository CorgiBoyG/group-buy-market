package cn.bugstack.domain.trade.model.aggregate;


import cn.bugstack.domain.trade.model.entity.PayActivityEntity;
import cn.bugstack.domain.trade.model.entity.PayDiscountEntity;
import cn.bugstack.domain.trade.model.entity.UserEntity;
import lombok.*;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.model.aggregate
 * @Description: 拼团订单聚合对象；聚合可以理解用各个四肢、身体、头等组装出来一个人
 * @Author: Daniel G
 * @Create: 2025-07-17 17:04:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderAggregate {

    /**
     * 用户实体对象
     */
    private UserEntity userEntity;
    /**
     * 支付活动实体对象
     */
    private PayActivityEntity payActivityEntity;
    /**
     * 支付优惠实体对象
     */
    private PayDiscountEntity payDiscountEntity;

}

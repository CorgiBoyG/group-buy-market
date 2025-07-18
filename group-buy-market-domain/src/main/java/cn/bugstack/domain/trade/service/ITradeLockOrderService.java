package cn.bugstack.domain.trade.service;


import cn.bugstack.domain.trade.model.entity.MarketPayOrderEntity;
import cn.bugstack.domain.trade.model.entity.PayActivityEntity;
import cn.bugstack.domain.trade.model.entity.PayDiscountEntity;
import cn.bugstack.domain.trade.model.entity.UserEntity;
import cn.bugstack.domain.trade.model.valobj.GroupBuyProgressVO;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service
 * @Description: 交易订单锁单服务接口
 * @Author: Daniel G
 * @Create: 2025-07-17 16:53:44
 */

public interface ITradeLockOrderService {

    /**
     * 查询，未被支付消费完成的营销优惠订单
     *
     * @param userId     用户ID
     * @param outTradeNo 外部唯一单号
     * @return 拼团，预购订单营销实体对象
     */
    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo);

    /**
     * 查询拼团进度
     *
     * @param teamId 拼团ID
     * @return 进度
     */
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    /**
     * 锁定，营销预支付订单；商品下单前，预购锁定。
     *
     * @param userEntity        用户根实体对象
     * @param payActivityEntity 拼团，支付活动实体对象
     * @param payDiscountEntity 拼团，支付优惠实体对象
     * @return 拼团，预购订单营销实体对象
     */
    MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity,
                                            PayDiscountEntity payDiscountEntity) throws Exception;
}

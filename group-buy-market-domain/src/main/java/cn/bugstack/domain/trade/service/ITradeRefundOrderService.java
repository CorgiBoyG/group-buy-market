package cn.bugstack.domain.trade.service;


import cn.bugstack.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundBehaviorEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundCommandEntity;
import cn.bugstack.domain.trade.model.valobj.TeamRefundSuccess;

import java.util.List;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service
 * @Description: 退单，逆向流程接口
 * @Author: Daniel G
 * @Create: 2025-07-27 10:17:07
 */

public interface ITradeRefundOrderService {

    TradeRefundBehaviorEntity refundOrder(TradeRefundCommandEntity tradeRefundCommandEntity) throws Exception;

    /**
     * 退单恢复锁单库存
     *
     * @param teamRefundSuccess 退单消息
     * @throws Exception 异常
     */
    void restoreTeamLockStock(TeamRefundSuccess teamRefundSuccess) throws Exception;

    /**
     * 查询超时未支付订单列表
     * 条件：当前时间不在活动时间范围内、状态为0（初始锁定）、out_trade_time为空
     *
     * @return 超时未支付订单列表，限制10条
     */
    List<UserGroupBuyOrderDetailEntity> queryTimeoutUnpaidOrderList();
}

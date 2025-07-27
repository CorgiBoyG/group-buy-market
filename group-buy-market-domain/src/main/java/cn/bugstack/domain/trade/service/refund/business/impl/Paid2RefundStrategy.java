package cn.bugstack.domain.trade.service.refund.business.impl;


import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.domain.trade.model.entity.TradeRefundOrderEntity;
import cn.bugstack.domain.trade.service.ITradeTaskService;
import cn.bugstack.domain.trade.service.refund.business.IRefundOrderStrategy;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.domain.trade.service.refund.business.impl
 * @Description: 发起退单（已支付、未成团），锁单量-1、完成量-1、组队订单状态更新、发送退单消息（MQ）
 * @Author: Daniel G
 * @Create: 2025-07-27 10:24:50
 */
@Slf4j
@Service("paid2RefundStrategy")
public class Paid2RefundStrategy implements IRefundOrderStrategy {

    @Resource
    private ITradeRepository repository;

    @Resource
    private ITradeTaskService tradeTaskService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;


    @Override
    public void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) throws Exception {
        log.info("退单；已支付未成团 userId:{} teamId:{} orderId:{}", tradeRefundOrderEntity.getUserId(),
                tradeRefundOrderEntity.getTeamId(), tradeRefundOrderEntity.getOrderId());

        /* 1. 退单，已支付&未成团*/
        NotifyTaskEntity notifyTaskEntity =
                repository.paid2Refund(GroupBuyRefundAggregate.buildPaid2RefundAggregate(tradeRefundOrderEntity, -1,
                        -1));

        /* 2. 发送HTTP/MQ消息，处理失败也会有定时任务补偿，通过这样异步线程池的方式，可以减轻任务调度，提高时效性*/
        if (null != notifyTaskEntity) {
            threadPoolExecutor.execute(() -> {
                Map<String, Integer> notifyResultMap = null;
                try {
                    notifyResultMap = tradeTaskService.execNotifyJob(notifyTaskEntity);
                    log.info("回调通知交易退单成功（已支付未成团） result:{}", JSON.toJSONString(notifyResultMap));
                } catch (Exception e) {
                    log.error("回调通知交易退单失败（已支付未成团） result:{}", JSON.toJSONString(notifyResultMap), e);
                    throw new AppException(e.getMessage());
                }
            });
        }
    }
}

package cn.bugstack.trigger.listener;


import cn.bugstack.domain.trade.model.valobj.TeamRefundSuccess;
import cn.bugstack.domain.trade.service.ITradeRefundOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.trigger.listener
 * @Description: 退单完成消息监听
 * @Author: Daniel G
 * @Create: 2025-07-27 16:44:38
 */
@Slf4j
@Component
public class RefundSuccessTopicListener {

    @Resource
    private ITradeRefundOrderService tradeRefundOrderService;

    /**
     * 此流程具备最终一致性；
     * 1. 数据库锁单量恢复完成，本地消息表补偿MQ，确保MQ消息一定会发送。
     * 2. MQ 消息消费，恢复锁单量库存。库存时添加分布式锁，确保不会重复操作。
     * 3. MQ 消息重试，确保在失败情况下，可以重复消息，又因为有分布式锁的处理，可以确保重复消费也不会重复添加锁单量库存。
     */
    // 绑定关系：交换机、队列、路由key的topic
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.producer.topic_team_refund.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.producer.exchange}", type =
                            ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.producer.topic_team_refund.routing_key}"
            )
    )
    public void listener(String message) {
        log.info("接收MQ消息-拼团退单成功 - 恢复拼团队伍锁单量:{}", message);

        TeamRefundSuccess teamRefundSuccess = JSON.parseObject(message, TeamRefundSuccess.class);
        try {
            tradeRefundOrderService.restoreTeamLockStock(teamRefundSuccess);
        } catch (Exception e) {
            log.info("接收MQ消息-拼团退单成功 - 恢复拼团队伍锁单量失败:{}", message, e);
            // 抛异常，mq消息会重试
            throw new RuntimeException(e);
        }
    }
}

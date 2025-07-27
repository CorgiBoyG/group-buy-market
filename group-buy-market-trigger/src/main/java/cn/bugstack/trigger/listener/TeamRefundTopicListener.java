package cn.bugstack.trigger.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.trigger.listener
 * @Description: 退单完成消息监听
 * @Author: Daniel G
 * @Create: 2025-07-27 16:44:38
 */
@Slf4j
@Component
public class TeamRefundTopicListener {

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
        log.info("接收MQ消息-拼团退单成功:{}", message);
    }
}

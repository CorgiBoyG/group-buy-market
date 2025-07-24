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
 * @Description: 结算完成消息监听
 * @Author: Daniel G
 * @Create: 2025-07-24 13:13:47
 */
@Slf4j
@Component
public class TeamSuccessTopicListener {

    // 绑定关系：交换机、队列、路由key的topic
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.producer.topic_team_success.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.producer.exchange}", type =
                            ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.producer.topic_team_success.routing_key}"
            )
    )
    public void listener(String message) {
        log.info("接收消息:{}", message);
    }

}

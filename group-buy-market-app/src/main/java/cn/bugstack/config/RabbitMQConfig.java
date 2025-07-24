package cn.bugstack.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.config
 * @Description: RabbitMQ配置
 * @Author: Daniel G
 * @Create: 2025-07-24 13:18:41
 */
//@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.config.producer.exchange}")
    private String exchangeName;

    /**
     * 专属交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding topicTeamSuccessBinding(
            @Value("${spring.rabbitmq.config.producer.topic_team_success.routing_key}") String routingKey,
            @Value("${spring.rabbitmq.config.producer.topic_team_success.queue}") String queue) {
        return BindingBuilder.bind(new Queue(queue, true))
                .to(topicExchange())
                .with(routingKey);
    }

}

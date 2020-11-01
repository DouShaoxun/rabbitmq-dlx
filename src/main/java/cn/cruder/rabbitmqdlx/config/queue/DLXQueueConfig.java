package cn.cruder.rabbitmqdlx.config.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 死信队列
 * @Author: Dsx
 * @Date: 2020-10-31 21:20
 * @Description: description
 */
@Configuration
public class DLXQueueConfig {
    /**
     * 队列名称
     */
    public static final String DLX_QUEUE_NAME = "dlx_queue";
    /**
     * 交换机名称
     */
    public static final String DLX_EXCHANGE_NAME = "dlx_exchange";
    /**
     * routingKey
     */
    public static final String DLX_ROUTING_KEY = "*.dlx";
    public static final String DLX_ROUTING_KEY_MSG = "msg.dlx";




    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue DLXQueue() {
        return QueueBuilder
                .durable(DLX_QUEUE_NAME)
                .build();
    }

    /**
     * 交换机
     *
     * @return
     */
    @Bean
    public TopicExchange DLXExchange() {
        return new TopicExchange(DLX_EXCHANGE_NAME);
    }

    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding DLXBinding() {
        return BindingBuilder
                .bind(DLXQueue())
                .to(DLXExchange())
                .with(DLX_ROUTING_KEY);
    }
}

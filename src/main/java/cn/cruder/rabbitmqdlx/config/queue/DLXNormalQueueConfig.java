package cn.cruder.rabbitmqdlx.config.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 正常的交换机
 *
 * @Author: Dsx
 * @Date: 2020-10-31 21:20
 * @Description: description
 */
@Configuration
public class DLXNormalQueueConfig {
    /**
     * 队列名称
     */
    public static final String DLX_NORMAL_QUEUE_NAME = "dlx_normal_queue";
    /**
     * 交换机名称
     */
    public static final String DLX_NORMAL_EXCHANGE_NAME = "dlx_normal_exchange";
    /**
     * routingKey
     */
    public static final String DLX_NORMAL_ROUTING_KEY = "*.dlx_normal";
    public static final String DLX_NORMAL_ROUTING_KEY_INFO = "info";
    public static final String DLX_NORMAL_ROUTING_KEY_ERROR = "error";
    /**
     * ttl时间(毫秒)
     */
    public static final int TTL_EXPIRATION = 10000;


    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue DLXNormalQueue() {
        return QueueBuilder
                .durable(DLX_NORMAL_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLXQueueConfig.DLX_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DLXQueueConfig.DLX_ROUTING_KEY_MSG)
                // 设置过期时间(单位毫秒) 让消息成为死信消息
                .ttl(TTL_EXPIRATION)
                // 设置队列长度(10)限制,超出长度的消息 直接投递到死信队列里
                .withArgument("x-max-length", 10)
                .build();
    }

    /**
     * 交换机
     *
     * @return
     */
    @Bean
    public TopicExchange DLXNormalExchange() {
        return new TopicExchange(DLX_NORMAL_EXCHANGE_NAME);
    }

    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding DLXNormalBinding() {
        return BindingBuilder
                .bind(DLXNormalQueue())
                .to(DLXNormalExchange())
                .with(DLX_NORMAL_ROUTING_KEY);
    }
}

package cn.cruder.rabbitmqdlx.listener;

import cn.cruder.rabbitmqdlx.config.queue.DLXQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听死信队列
 *
 * @Author: Dsx
 * @Date: 2020-11-01 10:15
 * @Description: description
 */
@Slf4j
@Component
public class TopicCustomerDLX {


    @RabbitListener(bindings = {
            @QueueBinding(
                    // 队列
                    value = @Queue(name = DLXQueueConfig.DLX_QUEUE_NAME),
                    // 交换机
                    exchange = @Exchange(type = "topic", name = DLXQueueConfig.DLX_EXCHANGE_NAME),
                    // routingKey
                    key = {DLXQueueConfig.DLX_ROUTING_KEY_MSG}
            )})
    public void receive(Message message, Channel channel) throws InterruptedException, IOException {
        Thread.sleep(1000);
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        log.info("死信队列:" + new String(message.getBody()));
        channel.basicAck(deliveryTag, true);
    }
}

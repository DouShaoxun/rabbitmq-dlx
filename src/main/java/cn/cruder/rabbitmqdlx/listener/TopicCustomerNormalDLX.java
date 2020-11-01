package cn.cruder.rabbitmqdlx.listener;

import cn.cruder.rabbitmqdlx.config.queue.DLXNormalQueueConfig;
import cn.cruder.rabbitmqdlx.config.queue.DLXQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 监听正常队列
 *
 * @Author: Dsx
 * @Date: 2020-11-01 10:15
 * @Description: description
 */
@Slf4j
@Component
public class TopicCustomerNormalDLX {

    private static int num = 0;


    @RabbitListener(bindings = {
            @QueueBinding(
                    // 队列  此处如果指定队列名称 回去检查队列是否已经创建 如果已经创建则会去检查类型是否完全一致,如果不一致，则此处会报错
                    //如果删除 queue 重新 declare 则不会有问题。或者通过 policy 来设置这个参数也可以不用删除队列
                    value = @Queue(name = DLXNormalQueueConfig.DLX_NORMAL_QUEUE_NAME,
                            arguments = {
                                    //Argument默认值类型为string 数字类型需手动指定
                                    @Argument(name = "x-dead-letter-exchange", value = DLXQueueConfig.DLX_EXCHANGE_NAME),
                                    @Argument(name = "x-dead-letter-routing-key", value = DLXQueueConfig.DLX_ROUTING_KEY_MSG),
                                    @Argument(name = "x-max-length", value = "10", type = "int"),
                                    @Argument(name = "x-message-ttl", value = "10000", type = "int"),
                            }),
                    //value = @Queue,//采用临时队列 貌似临时队列 下面的routingKey才生效
                    // 交换机
                    exchange = @Exchange(type = "topic", name = DLXNormalQueueConfig.DLX_NORMAL_EXCHANGE_NAME),
                    // routingKey
                    key = {DLXNormalQueueConfig.DLX_NORMAL_ROUTING_KEY_ERROR}
            )})
    public void receiveError(Message message, Channel channel) {
        long deliveryTag = 0;
        try {
            Thread.sleep(1000);
            // 签收一条消息 消息设置过期时间为10秒 没来得及消费的消息则会 投递到死信队列当中
            MessageProperties messageProperties = message.getMessageProperties();
            deliveryTag = messageProperties.getDeliveryTag();
            int i = 1 / 0;
            log.info("手动签收消息:" + new String(message.getBody()));
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            try {
                log.info(e.getMessage() + "手动拒绝消息:" + new String(message.getBody()));
                // 第三个三处设置为false  不让消息重回队列  消息会转发到 绑定的死信队列中
                channel.basicNack(deliveryTag, true, false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }


}

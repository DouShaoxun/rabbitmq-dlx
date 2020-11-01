package cn.cruder.rabbitmqdlx;

import cn.cruder.rabbitmqdlx.config.queue.DLXNormalQueueConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqDlxApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 死信队列测试：
     * 1.消息过期
     * 2.队列长度限制
     * 3.消息拒收:   channel.basicNack(deliveryTag, true, false); 第三个参数设置为false
     */


    @Test
    public void dlxError() {
        String msg = "";
        for (int i = 0; i < 1; i++) {
            msg = "hell dlx:" + i;
            rabbitTemplate.convertAndSend(DLXNormalQueueConfig.DLX_NORMAL_EXCHANGE_NAME,
                    DLXNormalQueueConfig.DLX_NORMAL_ROUTING_KEY_ERROR, msg);
        }
    }
}

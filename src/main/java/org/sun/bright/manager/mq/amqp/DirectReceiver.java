package org.sun.bright.manager.mq.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
// 监听的队列名称 directQueue
@RabbitListener(queues = "directQueue")
public class DirectReceiver {

    @RabbitHandler
    public void process(Map<String, Object> testMessage) {
        log.info("DirectReceiver消费者收到消息 : {}", testMessage.toString());
    }

}

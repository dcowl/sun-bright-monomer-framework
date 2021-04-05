package org.sun.bright.manager.mq.amqp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class AmqpProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitOperations rabbitOperations;

    public boolean sendForAmqp(String routingKey, Message message) {
        if (StringUtils.isEmpty(routingKey) && Objects.isNull(message)) {
            return false;
        }
        try {
            amqpTemplate.send(routingKey, message);
            return true;
        } catch (AmqpException e) {
            return false;
        }
    }

}

package org.sun.bright.manager.mq.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "topic.test", groupId = "topic.test.group1")
    public void topicTestConsumerGroup1(ConsumerRecord<?, ?> record, Acknowledgment ack,
                                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        Optional<?> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            log.info("topic.test.group1消费, Topic: {}, Message: {}", topic, msg);
            ack.acknowledge();
        }
    }

    @KafkaListener(topics = "topic.test", groupId = "topic.test.group2")
    public void topicTestConsumerGroup2(ConsumerRecord<?, ?> record, Acknowledgment ack,
                                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            log.info("topic.test.group2消费, Topic: {}, Message: {}", topic, msg);
            ack.acknowledge();
        }
    }
}

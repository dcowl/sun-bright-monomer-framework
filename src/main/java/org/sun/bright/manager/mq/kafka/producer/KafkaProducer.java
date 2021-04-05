package org.sun.bright.manager.mq.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Kafka生产者
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 */
@Slf4j
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaOperations<String, String> kafkaOperations;

    public boolean send(String topic, Object message) {
        return send(topic, JSON.toJSONString(message));
    }

    public boolean send(String topic, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        AtomicBoolean sendAtomicStatus = new AtomicBoolean(false);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info(" Kafka生产数据成功: topic name = [{}] ", topic);
                sendAtomicStatus.set(true);
            }

            @Override
            public void onFailure(@NonNull Throwable ex) {
                log.info(" Kafka生产数据异常, {} ", ex.getMessage(), ex);
            }
        });
        return sendAtomicStatus.get();
    }

    public boolean sendByKafkaOperations(String topic, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaOperations.send(topic, message);
        AtomicBoolean sendAtomicStatus = new AtomicBoolean(false);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info(" Kafka生产数据成功: topic name = [{}] ", topic);
                sendAtomicStatus.set(true);
            }

            @Override
            public void onFailure(@NonNull Throwable ex) {
                log.info(" Kafka生产数据异常, {} ", ex.getMessage(), ex);
            }
        });
        return sendAtomicStatus.get();
    }


}

package com.sharshag.springkafkaresearch.Listeners;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sharshag.springkafkaresearch.SamplePayload;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestConsumer {

    @KafkaListener(topics = "TestTopic", groupId = "test-group", concurrency = "1", containerFactory = "kafkaListenerContainerFactory")
    public void listen(
            ConsumerRecord<String, SamplePayload> consumerRecord) {

        log.info("Received message: {} on Thread: {}", consumerRecord.value(), Thread.currentThread().getId());
        simulateWork();
        if (consumerRecord.value().key().equals("2")) {
            delay();
        }
        log.info("Message processed: {} on Thread: {}", consumerRecord.value(), Thread.currentThread().getId());

    }

    public void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void simulateWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

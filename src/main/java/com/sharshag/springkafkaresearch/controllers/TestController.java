package com.sharshag.springkafkaresearch.controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharshag.springkafkaresearch.SamplePayload;
import com.sharshag.springkafkaresearch.service.KafkaDBTransactionService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class TestController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaDBTransactionService kafkaDBTransactionService;

    @PostMapping("/transactionalSend")
    public String postMethodName(@RequestBody String message) {
        
        kafkaDBTransactionService.saveAndNotify(message);
        return "Passed";
    }
    

    @PostMapping("/send/{topicName}")
    public ResponseEntity<String> postMessage(@PathVariable String topicName, @RequestBody SamplePayload payload) {
        RecordMetadata recordMetadata = null;
        String message = null;
        
        CompletableFuture<SendResult<String, Object>> future = null;
        //Send a simple message to the topic.
        // future = kafkaTemplate.send(topicName, payload);

        //Send a message to the topic with a key and payload
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, payload.key(),  payload);        
        future = kafkaTemplate.send(record);

        
        try {
            recordMetadata = future.get().getRecordMetadata();
            message = "Message sent to the topic: " + recordMetadata.topic() + "in partition: " + recordMetadata.partition() +  " with the offset " + recordMetadata.offset();

        } catch (InterruptedException | ExecutionException e) {
            
            message = "Error sending the message";
        }

        return ResponseEntity.ok().body(message);
        
    }
    
    public void sendMessage(String message) {
        
    }


}

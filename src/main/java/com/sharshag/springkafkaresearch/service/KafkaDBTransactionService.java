package com.sharshag.springkafkaresearch.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sharshag.springkafkaresearch.entities.SampleEntity;
import com.sharshag.springkafkaresearch.repositories.SampleEntityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaDBTransactionService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SampleEntityRepository sampleEntityRepository;
    @Qualifier("jpaTransactionManager")
    private final PlatformTransactionManager transactionManager;

    public void saveAndNotify(String message) {
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                SampleEntity entity = new SampleEntity();
                entity.setName(message);
                sampleEntityRepository.save(entity);
                kafkaOperations.send("TransactionTopic", message);
                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);
                throw e;
            }
            return null;
        });
    }

}

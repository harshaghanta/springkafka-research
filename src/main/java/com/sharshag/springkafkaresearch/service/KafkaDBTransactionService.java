package com.sharshag.springkafkaresearch.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharshag.springkafkaresearch.entities.SampleEntity;
import com.sharshag.springkafkaresearch.repositories.SampleEntityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaDBTransactionService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SampleEntityRepository sampleEntityRepository;
    // @Qualifier("jpaTransactionManager")
    // private final PlatformTransactionManager transactionManager;

    // public void saveAndNotify(String message) {
    //     kafkaTemplate.executeInTransaction(kafkaOperations -> {
    //         DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    //         TransactionStatus status = transactionManager.getTransaction(def);
    //         try {
    //             SampleEntity entity = new SampleEntity();
    //             entity.setName(message);
    //             sampleEntityRepository.save(entity);
    //             kafkaOperations.send("TransactionTopic", message);
    //             transactionManager.commit(status);
    //         } catch (Exception e) {
    //             transactionManager.rollback(status);
    //             throw e;
    //         }
    //         return null;
    //     });
    // }

    @Transactional("chainedTransactionManager")
    public void saveAndNotify(String message) {
        
        SampleEntity entity = new SampleEntity();
        entity.setName(message);
        sampleEntityRepository.save(entity);

        kafkaTemplate.send("TransactionTopic", message);

        if(message.length() > 5) {
            throw new RuntimeException("Message too long");
        }


    }

}

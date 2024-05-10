package com.sharshag.springkafkaresearch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharshag.springkafkaresearch.entities.SampleEntity;

public interface SampleEntityRepository extends JpaRepository<SampleEntity, Integer>{

}

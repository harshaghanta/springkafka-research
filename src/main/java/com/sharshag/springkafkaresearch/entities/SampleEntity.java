package com.sharshag.springkafkaresearch.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class SampleEntity {

    @Id
    private int id;
    @Column(name = "name", length = 10, nullable = false)
    private String name;
}

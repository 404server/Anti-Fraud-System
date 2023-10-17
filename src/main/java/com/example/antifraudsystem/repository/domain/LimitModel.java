package com.example.antifraudsystem.repository.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "limit_table")
public class LimitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "limit_seq")
    @SequenceGenerator(name = "limit_seq", sequenceName = "limit_seq", allocationSize = 1)
    private Long id;
    @Column(name = "max_allowed")
    private long maxAllowed;
    @Column(name = "max_manual")
    private long maxManual;

    public LimitModel() {
        this.maxAllowed = 200;
        this.maxManual = 1500;
    }

}
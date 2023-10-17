package com.example.antifraudsystem.repository.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "suspicious_ip")
public class IpModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ip_seq")
    @SequenceGenerator(name = "ip_seq", sequenceName = "ip_seq", allocationSize = 1)
    private Long id;
    @Column(name = "ip")
    private String ip;

    public IpModel() {
    }

    public IpModel(String ip) {
        this.ip = ip;
    }

}


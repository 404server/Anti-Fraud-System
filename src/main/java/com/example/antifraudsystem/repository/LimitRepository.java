package com.example.antifraudsystem.repository;


import com.example.antifraudsystem.repository.domain.LimitModel;
import org.springframework.data.repository.CrudRepository;

public interface LimitRepository extends CrudRepository<LimitModel, Long> {

    default LimitModel getLimitModelById(Long id) {
        return findById(id).orElse(new LimitModel());
    }


}


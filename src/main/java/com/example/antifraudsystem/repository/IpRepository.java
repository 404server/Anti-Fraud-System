package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.repository.domain.IpModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpRepository extends CrudRepository<IpModel, Long> {
    boolean existsByIp(String ip);

    IpModel findByIp(String ip);

    List<IpModel> findByOrderByIdAsc();
}

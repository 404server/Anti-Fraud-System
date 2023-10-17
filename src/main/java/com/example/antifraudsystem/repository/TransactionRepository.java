package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.repository.domain.TransactionModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionModel, Long> {
    @Query("SELECT COUNT(DISTINCT t.region) FROM TransactionModel t WHERE t.region <> ?1 AND t.number = ?2 AND t.date BETWEEN ?3 AND ?4")
    long getDistinctRegion(String region,
                           String number,
                           Timestamp start,
                           Timestamp end);

    @Query("SELECT COUNT(DISTINCT t.ip) FROM TransactionModel t WHERE t.ip <> ?1 AND t.number = ?2 AND t.date BETWEEN ?3 AND ?4")
    long getDistinctIp(String ip,
                       String number,
                       Timestamp start,
                       Timestamp end);

    @Query("SELECT t FROM TransactionModel t WHERE t.transactionId = ?1")
    TransactionModel getModelByID(Long id);

    boolean existsByTransactionId(Long id);

    List<TransactionModel> findByOrderByTransactionIdAsc();

    @Query("SELECT t FROM TransactionModel t WHERE t.number = ?1 ORDER BY t.transactionId ASC")
    List<TransactionModel> getTransactionsByNumber(String number);

}

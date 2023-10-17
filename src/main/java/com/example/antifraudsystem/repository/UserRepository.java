package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.repository.domain.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    boolean existsByUsername(String username);

    UserModel findUserByUsername(String username);

    Optional<UserModel> findAppUserByUsername(String username);

    List<UserModel> findByOrderByIdAsc();

    @Transactional
    void deleteByUsername(String username);
}

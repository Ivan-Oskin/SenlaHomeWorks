package com.oskin.bank.repository;

import com.oskin.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE accounts_id_seq RESTART WITH 1", nativeQuery = true)
    void resetSequenceToOne();
}

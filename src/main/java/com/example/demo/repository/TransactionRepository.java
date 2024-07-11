package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT c from Transaction c where c.fromAccNo = :accountNumber or c.toAccNo = :accountNumber order by c.transactionDate desc")
    List<Transaction> findTransactionsByFromAccountIdOrToAccountId(String accountNumber);
}

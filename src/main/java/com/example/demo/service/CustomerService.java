package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InSufficientBalanceException;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    
    Optional<Customer> getCustomerById(Long id);


    Optional<Account> getAccountByCustomerId(Long customerId);

    Account deposit(String accountId, Double amount) throws AccountNotFoundException;

    Account withdraw(String accountId, Double amount) throws AccountNotFoundException;

    Account transfer(String fromAccountId, String toAccountId, Double amount) throws AccountNotFoundException, InSufficientBalanceException;

    List<Transaction> getTransactions(Long id) throws AccountNotFoundException;

    List<Transaction> getTransactionsByPage(int pageNo, int limit, String field, String order);
}
package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.enums.TransactionType;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InSufficientBalanceException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Account> getAccountByCustomerId(Long customerId) {
        return Optional.ofNullable(accountRepository.findAccountByCustomerId(customerId));
    }

    @Override
    public Account deposit(String accNo, Double amount) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accNo)
                .orElseThrow(() ->{
                    log.error("Source account not found");
                    return new AccountNotFoundException("Source account not found");
                });
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        Account savedAcc = accountRepository.save(account);
        createAndSaveTransaction(TransactionType.DEPOSIT, amount, savedAcc.getAccountNumber(), savedAcc.getAccountNumber());
        return savedAcc;
    }

    @Override
    public Account withdraw(String accNo, Double amount) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accNo)
                .orElseThrow(() -> {
                    log.error("Source account not found");
                    return new AccountNotFoundException("Source account not found");
                });

        double currentBalance = account.getBalance();

        if (currentBalance < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        double newBalance = currentBalance - amount;
        account.setBalance(newBalance);

        Account savedAcc = accountRepository.save(account);
        createAndSaveTransaction(TransactionType.WITHDRAWAL, amount, savedAcc.getAccountNumber(), savedAcc.getAccountNumber());
        return savedAcc;
    }

    @Override
    public Account transfer(String fromAccNo, String toAccNo, Double amount) throws AccountNotFoundException, InSufficientBalanceException {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccNo)
                .orElseThrow(() -> {
                    log.error("Source account not found");
                    return new AccountNotFoundException("Source account not found");
                });

        Account toAccount = accountRepository.findByAccountNumber(toAccNo)
                .orElseThrow(() -> {
                    log.error("Destination account not found");
                    return new AccountNotFoundException("Destination account not found :"+ toAccNo);
                });

        double currentBalance = fromAccount.getBalance();

        if (currentBalance < amount) {
            log.error("Insufficient balance in source account");
            throw new InSufficientBalanceException("Insufficient balance in source account");
        }

        double newBalanceFromAccount = currentBalance - amount;
        fromAccount.setBalance(newBalanceFromAccount);

        double newBalanceToAccount = toAccount.getBalance() + amount;
        toAccount.setBalance(newBalanceToAccount);

        accountRepository.save(fromAccount);
        log.debug("Source account updated successfully : {}", fromAccount);
        accountRepository.save(toAccount);
        log.debug("Destination account updated successfully : {}", toAccount);

        createAndSaveTransaction(TransactionType.TRANSFER, amount, fromAccount.getAccountNumber(), toAccount.getAccountNumber());
        return fromAccount;
    }

    @Override
    public List<Transaction> getTransactions(Long id) throws AccountNotFoundException {
        Account acc = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return transactionRepository.findTransactionsByFromAccountIdOrToAccountId(acc.getAccountNumber());
    }


    public Transaction createAndSaveTransaction(TransactionType type, Double amount, String fromAccNo, String toAccNo) {
        Transaction transaction = new Transaction();
        transaction.setType(type.getType());
        transaction.setAmount(amount);
        transaction.setFromAccNo(fromAccNo);
        transaction.setToAccNo(toAccNo);
        transaction.setTransactionDate(LocalDateTime.now());
        log.debug("Transaction created successfully : {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByPage(int pageNo, int limit, String field, String order)
    {
        if (order.equals("desc"))
        {
            Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(field).descending());
            return transactionRepository.findAll(pageable).getContent();
        }
        Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(field).ascending());
        return transactionRepository.findAll(pageable).getContent();
    }
}
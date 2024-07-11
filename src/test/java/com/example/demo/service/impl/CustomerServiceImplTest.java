package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InSufficientBalanceException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1L);

        assertEquals(1L, result.get().getId());
    }

    //generate test method for getAccountByCustomerId
    @Test
    void testGetAccountByCustomerId() {
        Account account = new Account();
        account.setId(1L);

        when(accountRepository.findAccountByCustomerId(1L)).thenReturn(account);

        Optional<Account> result = customerService.getAccountByCustomerId(1L);

        assertEquals(1L, result.get().getId());
    }

    //generate test method for deposit
    @Test
    void testDeposit() throws AccountNotFoundException {
        Customer customer = new Customer();
        customer.setId(1L);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(100.0);
        account.setAccountNumber("123456");
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(new Transaction());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findByAccountNumber(Mockito.anyString())).thenReturn(Optional.of(account));
        Account result = customerService.deposit("123456",10.0);
        assertEquals(110.0, result.getBalance());
    }

    @Test
    void testWithDraw() throws AccountNotFoundException {
        Customer customer = new Customer();
        customer.setId(1L);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(100.0);
        account.setAccountNumber("123456");
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(new Transaction());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findByAccountNumber(Mockito.anyString())).thenReturn(Optional.of(account));
        Account result = customerService.withdraw("123456",10.0);
        assertEquals(90.0, result.getBalance());
    }

    @Test
    void testTransfer() throws AccountNotFoundException, InSufficientBalanceException {
        Customer customer = new Customer();
        customer.setId(1L);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(100.0);
        account.setAccountNumber("123456");
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(new Transaction());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findByAccountNumber(Mockito.anyString())).thenReturn(Optional.of(account));
        Account result = customerService.transfer("123456","123456",10.0);
        assertEquals(100.0, result.getBalance());
    }

    @Test
    void testGetTransactions() throws AccountNotFoundException {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.findTransactionsByFromAccountIdOrToAccountId(Mockito.anyString())).thenReturn(Arrays.asList(new Transaction()));
        List<Transaction> result = customerService.getTransactions(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateAndSaveTransaction() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Optional<Customer> result = customerService.getCustomerById(1L);
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetTransactionsByPage() {
        Page<Transaction> page = Mockito.mock(Page.class);
        when(page.getContent()).thenReturn(Arrays.asList(new Transaction()));
        when(transactionRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        List<Transaction> result = customerService.getTransactionsByPage(1,1,"name","asc");
        assertEquals(1, result.size());
    }

    @Test
    void testGetTransactionsByPageDesc() {
        Page<Transaction> page = Mockito.mock(Page.class);
        when(page.getContent()).thenReturn(Arrays.asList(new Transaction()));
        when(transactionRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        List<Transaction> result = customerService.getTransactionsByPage(1,1,"name","desc");
        assertEquals(1, result.size());
    }


}
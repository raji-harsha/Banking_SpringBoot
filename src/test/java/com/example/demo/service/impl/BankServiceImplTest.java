package com.example.demo.service.impl;

import com.example.demo.dto.TransactionData;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class BankServiceImplTest {

    @InjectMocks
    private BankServiceImpl bankService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() throws CustomerNotFoundException, UnAuthorizedException {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setPassword("password");

        TransactionData transactionData = new TransactionData();
        transactionData.setCustomerId(1L);
        transactionData.setPassword("password");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = bankService.login(transactionData);

        assertEquals(1L, result.getId());
    }

    @Test
    void testLoginWithInvalidPassword() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setPassword("password");

        TransactionData transactionData = new TransactionData();
        transactionData.setCustomerId(1L);
        transactionData.setPassword("wrong_password");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(UnAuthorizedException.class, () -> bankService.login(transactionData));
    }
    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setPassword("password");
        Account account = new Account();
        when(accountRepository.save(account)).thenReturn(account);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = bankService.createCustomer(customer);

        assertEquals(1L, result.getId());
    }

}
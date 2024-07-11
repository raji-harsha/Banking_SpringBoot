package com.example.demo.service.impl;

import com.example.demo.dto.TransactionData;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.BankService;
import com.example.demo.util.BankingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankServiceImpl implements BankService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Customer login(TransactionData transactionData) throws CustomerNotFoundException, UnAuthorizedException {
        Customer customer = customerRepository.findById(transactionData.getCustomerId()).orElseThrow(() -> {
            log.error("Customer with id: {} not found", transactionData.getCustomerId());
            return new CustomerNotFoundException("Customer with id: " + transactionData.getCustomerId() + " not found");
        });

        if (!customer.getPassword().equals(transactionData.getPassword())) {
            log.error("Invalid password for customer with id: {}", transactionData.getCustomerId());
            throw new UnAuthorizedException("Invalid password for customer with id: " + transactionData.getCustomerId());
        }
        log.debug("Customer logged in successfully : {}", customer);

        return customer;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        log.debug("Customer saved successfully : {}", savedCustomer);
        Account account = new Account();
        account.setAccountNumber(BankingUtils.generateAccountNumber());
        account.setCustomer(savedCustomer);
        accountRepository.save(account);
        log.debug("Account created successfully : {}", account);
        return savedCustomer;
    }
}

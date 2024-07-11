package com.example.demo.service;

import com.example.demo.dto.TransactionData;
import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.UnAuthorizedException;

public interface BankService {

    Customer login(TransactionData transactionData) throws CustomerNotFoundException, UnAuthorizedException;

    Customer createCustomer(Customer customer);
}
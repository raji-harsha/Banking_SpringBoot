package com.example.demo.controller;

import com.example.demo.dto.TransactionData;
import com.example.demo.entity.AdminAccount;
import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.service.impl.BankServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankServiceImpl bankServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestBody TransactionData transactionData) {
        try {
            Customer customer = bankServiceImpl.login(transactionData);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (CustomerNotFoundException | UnAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/adminLogin")
    public ResponseEntity<Customer> adminLogin(@RequestBody TransactionData transactionData) {
        AdminAccount adminAccount = new AdminAccount();
        if (adminAccount.getUsername().equals(transactionData.getUserName()) && adminAccount.getPassword().equals(transactionData.getPassword())) {
            return new ResponseEntity<>(new Customer(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/createCustomer")
    public Customer createCustomer(@RequestBody Customer customer) {
        return bankServiceImpl.createCustomer(customer);
    }


}
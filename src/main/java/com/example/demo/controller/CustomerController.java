package com.example.demo.controller;

import com.example.demo.dto.TransactionData;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InSufficientBalanceException;
import com.example.demo.service.CustomerService;
import com.example.demo.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccountByCustomerId(@PathVariable Long id) {
        return customerService.getAccountByCustomerId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/account/deposit")
    public Account deposit(@RequestBody TransactionData data) throws AccountNotFoundException {
        kafkaProducerService.sendMessage("transactions", "Amount : INR "+ data.getAmount() +", Deposit made to account " + data.getFromAccountNumber());
        return customerService.deposit(data.getFromAccountNumber(), data.getAmount());
    }

    @PostMapping("/account/withdraw")
    public Account withdraw(@RequestBody TransactionData data) throws AccountNotFoundException {
        kafkaProducerService.sendMessage("transactions", "Amount : INR "+ data.getAmount() +", Withdraw made from account " + data.getFromAccountNumber());
        return customerService.withdraw(data.getFromAccountNumber(), data.getAmount());
    }

    @PostMapping("/account/transfer")
    public ResponseEntity transfer(@RequestBody TransactionData data) {
        try{
            kafkaProducerService.sendMessage("transactions", "Amount : INR "+ data.getAmount() +", Transfer made from account " + data.getFromAccountNumber()+ " to account "+data.getToAccountNumber());
            return new ResponseEntity(customerService.transfer(data.getFromAccountNumber(), data.getToAccountNumber(), data.getAmount()), HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (InSufficientBalanceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/account/transactions/{id}")
    public ResponseEntity<List<Transaction>> transactions(@PathVariable Long id) throws AccountNotFoundException {
        return new ResponseEntity<>(customerService.getTransactions(id), HttpStatus.OK);
    }

    @GetMapping("/account/transactions/page/{pageNo}/{limit}/{field}/{order}")
    public List<Transaction> getTransactionsByPage(@PathVariable int pageNo, @PathVariable int limit,
                                           @PathVariable String field, @PathVariable String order)
    {

        return customerService.getTransactionsByPage(pageNo, limit, field, order);
    }


}
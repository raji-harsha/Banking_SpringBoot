package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.projections.CustomerDTO;
import com.example.demo.repository.CustomerViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CacheConfig(cacheNames = "customer")
@RestController
@RequestMapping("/admin")
@RestControllerAdvice
public class AdminController {

    @Autowired
    private CustomerViewRepository customerViewRepository;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerViewRepository.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customerByName/{name}")
    public ResponseEntity<CustomerDTO> getCustomerByName(@PathVariable String name) throws CustomerNotFoundException {
        CustomerDTO loginDetails = customerViewRepository.findByName(name).orElseThrow(()->new CustomerNotFoundException("Customer not found with name: "+name));
        return new ResponseEntity<>(loginDetails, HttpStatus.OK);
    }

    @Cacheable(key = "#id")
    @GetMapping("/customer/{id}")
    public Customer getCustomerById(@PathVariable long id) {
        return customerViewRepository.findById(id).get();
    }

    @CachePut(key = "#id")
    @PutMapping("/customer/{id}")
    public Customer updateCustomer(@PathVariable long id,@RequestBody Customer vehicle){
        return customerViewRepository.save(vehicle);
    }

    @CacheEvict(key = "#id")
    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable long id){
        customerViewRepository.deleteById(id);
    }
}
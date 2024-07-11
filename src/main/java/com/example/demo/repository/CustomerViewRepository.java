package com.example.demo.repository;

import com.example.demo.entity.Customer;
import com.example.demo.projections.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerViewRepository extends JpaRepository<Customer, Long> {

    Optional<CustomerDTO> findByName(String name);
}

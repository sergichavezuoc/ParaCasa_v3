package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  List<Customer> findByName(String name);
  List<Customer> findBySurname(String title);
  List<Customer> findByEmail(String email);
  @Transactional
  void deleteById(long customerId);
}

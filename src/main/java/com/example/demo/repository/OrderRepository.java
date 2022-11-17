package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
  //List<Order> findMenusByorders(Long orderId);
  List<Order> findAllByUser_id(Long userId);
  void deleteById(long orderlId);
}

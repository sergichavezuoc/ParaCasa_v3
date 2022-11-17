package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Menu;
import com.example.demo.model.Order;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  List<Menu> findByNameContaining(String name);
  List<Menu> findByRestaurantId(Long postId);
  List<Menu> findMenusByorders(Order order);
  void deleteByRestaurantId(long restuarantlId);
}

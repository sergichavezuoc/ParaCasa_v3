package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.model.MenuType;

public interface MenuTypeRepository extends JpaRepository<MenuType, Long> {
  Optional<MenuType> findById(Long postId);
}

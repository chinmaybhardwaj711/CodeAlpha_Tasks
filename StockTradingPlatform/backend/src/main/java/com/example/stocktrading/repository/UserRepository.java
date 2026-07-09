package com.example.stocktrading.repository;

import com.example.stocktrading.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

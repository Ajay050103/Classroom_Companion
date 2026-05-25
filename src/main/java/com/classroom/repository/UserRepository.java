package com.classroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classroom.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);
    User findByName(String name);
}
package com.rayokross.academy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rayokross.academy.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
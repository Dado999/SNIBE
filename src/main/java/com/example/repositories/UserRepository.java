package com.example.repositories;

import com.example.exceptions.NotFoundException;
import com.example.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username) throws NotFoundException;

}

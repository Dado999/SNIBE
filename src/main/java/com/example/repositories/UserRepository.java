package com.example.repositories;

import com.example.exceptions.NotFoundException;
import com.example.models.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username) throws NotFoundException;
    @Query(value = "SELECT * FROM user u WHERE u.username = :username OR u.email = :email LIMIT 1", nativeQuery = true)
    Optional<User> findFirstByUsernameOrEmail(@Param("username") String username, @Param("email") String email) throws NotFoundException;
}

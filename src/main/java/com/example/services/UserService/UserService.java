package com.example.services.UserService;

import com.example.exceptions.NotFoundException;
import com.example.exceptions.UsernameAlreadyExists;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.User;
import com.example.repositories.UserRepository;
import com.example.services.AuthService.AuthService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDTO findByUsername(String username) throws NotFoundException {
        logger.info("Attempting to find user by username: {}", username);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            logger.info("User found: {}", username);
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            logger.warn("User not found: {}", username);
            return null;
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.info("User successfully loaded: {}", username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .accountLocked(user.getReguser() == 1)
                .password(user.getPassword())
                .authorities(user.getPermission())
                .roles(user.getRole())
                .build();
    }

    @Transactional
    public UserDTO insert(UserDTO object) throws UsernameAlreadyExists, NotFoundException {
        logger.info("Attempting to insert new user: {}", object.getUsername());

        userRepository.findFirstByUsernameOrEmail(object.getUsername(), object.getEmail())
                .ifPresent(existingUser -> {
                    logger.error("Username or email already exists: {}", object.getUsername());
                    throw new UsernameAlreadyExists();
                });

        User entity = modelMapper.map(object, User.class);
        entity.setId(null);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        entity = userRepository.saveAndFlush(entity);
        entityManager.refresh(entity);

        logger.info("User successfully inserted: {}", object.getUsername());
        return modelMapper.map(entity, UserDTO.class);
    }

    public List<UserDTO> getUnregisteredUsers() {
        logger.info("Fetching all unregistered users...");
        Optional<List<User>> unregisteredUserEntities = userRepository.getUserByReguser((byte) 0);

        if (unregisteredUserEntities.isPresent()) {
            logger.info("Unregistered users found: {}", unregisteredUserEntities.get().size());
            return unregisteredUserEntities.get().stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .toList();
        } else {
            logger.warn("No unregistered users found.");
            return new ArrayList<>();
        }
    }

    public UserDTO findById(Integer id) {
        logger.info("Attempting to find user by ID: {}", id);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            logger.info("User found by ID: {}", id);
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            logger.warn("User not found by ID: {}", id);
            return null;
        }
    }

    public String updateUser(UserDTO userDTO) {
        logger.info("Attempting to update user with ID: {}", userDTO.getIduser());

        Optional<User> optionalUser = userRepository.findById(userDTO.getIduser());
        if (optionalUser.isEmpty()) {
            logger.error("User not found with ID: {}", userDTO.getIduser());
            return "User not found";
        }

        User user = optionalUser.get();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setReguser(userDTO.getReguser());
        user.setPermission(userDTO.getPermission());
        user.setRole(userDTO.getRole());

        userRepository.saveAndFlush(user);
        logger.info("User successfully updated: {}", userDTO.getIduser());
        return "User updated successfully";
    }
}


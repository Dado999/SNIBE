package com.example.services.UserService;

import com.example.exceptions.NotFoundException;
import com.example.exceptions.UsernameAlreadyExists;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.User;
import com.example.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public UserDTO findByUsername(String username) throws NotFoundException {
       Optional<User> user =  userRepository.findByUsername(username);
       if(user.isPresent())
           return modelMapper.map(user,UserDTO.class);
        else return null;
   }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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
        userRepository.findFirstByUsernameOrEmail(object.getUsername(), object.getEmail())
                .ifPresent(existingUser -> { throw new UsernameAlreadyExists(); });

        User entity = modelMapper.map(object, User.class);
        entity.setId(null);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity = userRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
        return modelMapper.map(entity, UserDTO.class);
    }
}

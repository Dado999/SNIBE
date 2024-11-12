package com.example.services.UserService;

import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.User;
import com.example.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    public UserDTO findByUsername(String username) throws NotFoundException {
       Optional<User> user =  userRepository.findByUsername(username);
       if(user.isPresent())
           return modelMapper.map(user,UserDTO.class);
        else return null;
   }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            return user.map(value -> org.springframework.security.core.userdetails.User
                    .withUsername(value.getUsername())
                    .accountLocked(value.getReguser() == 1)
                    .password(value.getPassword())
                    .authorities(value.getPermission())
                    .roles(value.getRole())
                    .build()).orElse(null);

        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public UserDTO insert(UserDTO object) {
        User entity = modelMapper.map(object,User.class);
        entity.setId(null);
        entity = userRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
        return modelMapper.map(entity,UserDTO.class);
    }
}

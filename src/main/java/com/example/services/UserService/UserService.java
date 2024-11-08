package com.example.services.UserService;

import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.User;
import com.example.repositories.UserRepository;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            return user.map(value -> org.springframework.security.core.userdetails.User
                    .withUsername(value.getUsername())
                    .accountLocked(value.getReguser() == 1)
                    .password(value.getPassword())
                    .authorities(value.getRole())
                    .roles(value.getPermission())
                    .build()).orElse(null);

        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }


//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
//        super(userRepository, modelMapper, User.class);
//        this.userRepository = userRepository;
//        this.modelMapper = modelMapper;
//    }
//    @Override
//    public UserDTO findByUsername(String username) throws NotFoundException {
//        User user =  userRepository.findByUsername(username);
//        if(user != null)
//            return modelMapper.map(user,UserDTO.class);
//        else return null;
//    }
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        try {
//            User user = userRepository.findByUsername(username);
//            return org.springframework.security.core.userdetails.User
//                    .withUsername(user.getUsername())
//                    .accountLocked(user.getReguser() == 1)
//                    .password(user.getPassword())
//                    .authorities(user.getRole())
//                    .roles(user.getPermission())
//                    .build();
//
//        } catch (NotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

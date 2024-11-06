package com.example.services.UserService;

import com.example.base.CrudJpaService;
import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.LoginDTO;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.JwtResponse;
import com.example.models.entities.User;
import com.example.repositories.UserRepository;
import com.example.config.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CrudJpaService<User,Integer> implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        super(userRepository, modelMapper, User.class);
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public UserDTO findByUsername(String username) throws NotFoundException {
        User user =  userRepository.findByUsername(username);
        if(user != null)
            return modelMapper.map(user,UserDTO.class);
        else return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .accountLocked(user.getReguser() == 1)
                    .password(user.getPassword())
                    .authorities(user.getRole())
                    .roles(user.getPermission())
                    .build();

        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

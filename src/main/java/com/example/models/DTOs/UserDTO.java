package com.example.models.DTOs;

import com.example.models.entities.Comment;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserDTO{
    private Integer id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private Byte reguser;
    private String permission;
    private String role;
    private List<Comment> commentsByIduser;
}

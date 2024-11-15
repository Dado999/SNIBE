package com.example.models.DTOs;

import com.example.models.entities.Comment;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO{
    private Integer iduser;
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

package com.example.models.DTOs;

import com.example.models.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDTO {

    private Integer id;
    private String content;
    private Timestamp date;
    private String category;
    private User iduser;
}
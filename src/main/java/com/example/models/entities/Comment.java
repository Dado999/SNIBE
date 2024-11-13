package com.example.models.entities;

import com.example.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
public class Comment implements BaseEntity<Integer> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idcomment", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "content", nullable = false, length = 100)
    private String content;
    @Basic
    @Column(name = "date", nullable = false)
    private Timestamp date;
    @ManyToOne
    @JoinColumn(name = "iduser", referencedColumnName = "iduser", nullable = false)
    private User iduser;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(content, comment.content) && Objects.equals(date, comment.date) && Objects.equals(iduser, comment.iduser);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, content, date, iduser);
    }
}

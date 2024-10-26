package com.example.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "idcomment", nullable = false)
    private Integer id;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_idTopics", nullable = false)
    private com.example.sni.Topic topicIdtopics;

}
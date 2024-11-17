package com.example.repositories;

import com.example.models.entities.Comment;
import com.example.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByCategoryAndApprovedOrderByDateDesc(String category, Integer approved, Pageable pageable);

    Optional<List<Comment>> findByApproved(Integer approved);
}


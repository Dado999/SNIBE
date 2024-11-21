package com.example.services.CommentService;

import com.example.models.DTOs.CommentDTO;
import com.example.models.entities.Comment;
import com.example.repositories.CommentRepository;
import com.example.services.AuthService.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public Page<CommentDTO> getByCategory(String category, int page, int size) {
        logger.info("Fetching comments for category: {}, page: {}, size: {}", category, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentEntities = commentRepository.findByCategoryAndApprovedOrderByDateDesc(category, 1, pageable);
        logger.debug("Retrieved {} comments for category: {}", commentEntities.getTotalElements(), category);
        return commentEntities.map(comment -> modelMapper.map(comment, CommentDTO.class));
    }

    public List<CommentDTO> findUnapprovedComments() {
        logger.info("Fetching all unapproved comments");
        Optional<List<Comment>> unapprovedCommentEntities = commentRepository.findByApproved(0);
        if (unapprovedCommentEntities.isPresent()) {
            List<CommentDTO> unapprovedCommentDTOs = new ArrayList<>();
            for (Comment c : unapprovedCommentEntities.get()) {
                unapprovedCommentDTOs.add(modelMapper.map(c, CommentDTO.class));
            }
            logger.info("Found {} unapproved comments", unapprovedCommentDTOs.size());
            return unapprovedCommentDTOs;
        }
        logger.warn("No unapproved comments found");
        return null;
    }

    public void deleteComment(Integer id) {
        logger.info("Attempting to delete comment with ID: {}", id);
        commentRepository.deleteById(id.longValue());
        logger.info("Comment with ID: {} deleted successfully", id);
    }

    public String updateComment(Integer id, String newContent) {
        logger.info("Attempting to update comment with ID: {}", id);
        Comment comment = commentRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> {
                    logger.error("Comment not found with ID: {}", id);
                    return new EntityNotFoundException("Comment not found with ID: " + id);
                });
        comment.setContent(newContent);
        commentRepository.saveAndFlush(comment);
        logger.info("Comment with ID: {} updated successfully", id);
        return "Comment updated successfully!";
    }

    public String insertComment(CommentDTO comment) {
        logger.info("Inserting a new comment");
        Comment commentEnt = modelMapper.map(comment, Comment.class);
        commentEnt.setId(null);
        Comment savedComment = commentRepository.saveAndFlush(commentEnt);
        if (savedComment.getId() != null) {
            logger.info("Comment inserted successfully with ID: {}", savedComment.getId());
            return "New comment added successfully!";
        } else {
            logger.error("Failed to insert the comment");
            throw new RuntimeException("Failed to insert the comment.");
        }
    }

    public String approveComment(Integer id) {
        logger.info("Attempting to approve comment with ID: {}", id);
        Optional<Comment> comment = commentRepository.findById(Long.valueOf(id));
        if (comment.isEmpty()) {
            logger.warn("Comment not found with ID: {}", id);
            return "Comment not found!";
        } else {
            Comment approvedComment = comment.get();
            approvedComment.setApproved(1);
            commentRepository.saveAndFlush(approvedComment);
            logger.info("Comment with ID: {} approved successfully", id);
            return "Successfully approved!";
        }
    }
}


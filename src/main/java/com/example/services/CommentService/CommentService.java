package com.example.services.CommentService;

import com.example.models.DTOs.CommentDTO;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.Comment;
import com.example.models.entities.User;
import com.example.repositories.CommentRepository;
import com.example.services.UserService.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public Page<CommentDTO> getByCategory(String category, int page, int size) {
        // Create a Pageable object with the given page and size
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated Comment entities from the repository
        Page<Comment> commentEntities = commentRepository.findByCategoryAndApprovedOrderByDateDesc(category,1,pageable);

        // Convert each Comment entity into a CommentDTO using the model mapper
        return commentEntities.map(comment -> modelMapper.map(comment, CommentDTO.class));
    }

    public List<CommentDTO> findUnapprovedComments(){
        Optional<List<Comment>> unapprovedCommentEntities = commentRepository.findByApproved(0);
        List<CommentDTO> unapprovedCommentDTOs = new ArrayList<>();
        if(unapprovedCommentEntities.isPresent()) {
            for (Comment c :unapprovedCommentEntities.get())
                unapprovedCommentDTOs.add(modelMapper.map(c,CommentDTO.class));
            return unapprovedCommentDTOs;
        }
        return null;
    }
    public void deleteComment(Integer id){
        this.commentRepository.deleteById(id.longValue());
    }

    public String updateComment(Integer id, String newContent) {
        Comment comment = commentRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + id));
        comment.setContent(newContent);
        commentRepository.saveAndFlush(comment);
        return "Comment updated successfully!";
    }

    public String insertComment(CommentDTO comment){
        Comment commentEnt = modelMapper.map(comment,Comment.class);
        commentEnt.setId(null);
        commentRepository.saveAndFlush(commentEnt);
        return "New comment added successfully!";
    }
}

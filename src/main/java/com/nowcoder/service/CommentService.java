package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;


    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public void deleteComment(int commentId){
        commentDAO.deleteComment(commentId);
    }

    public int getCommentCount(int entityType, int entityId){
        return commentDAO.commentCount(entityType ,entityId);
    }

    public List<Comment> getCommentsByEntity(int entityType, int entityId){
        return commentDAO.selectByEntity(entityType, entityId);
    }


}

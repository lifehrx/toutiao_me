package com.nowcoder.controller;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Controller
public class CommentController {
    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@Param("content") String content, @Param("newsId") int newsId){

        try {
            content = HtmlUtils.htmlEscape(content);
            Comment comment = new Comment();
            comment.setContent(content);
            // comment.setId(newsId);
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_NEWS);// 这里写错了，造成news 下得 评论列表无法显示
            comment.setEntityId(newsId);
            comment.setStatus(0);
/*
            if (hostHolder.getUser() != null){
                comment.setUserId(hostHolder.getUser().getId());
            }
*/


            commentService.addComment(comment);
            int commentCount = commentService.getCommentCount(comment.getEntityType(), comment.getEntityId());
            newsService.updateCommentCount(comment.getEntityId(),commentCount);

        } catch (Exception e) {
            logger.error("添加评论失败："+ e.getMessage());
        }

        return "redirect: /news/" + String.valueOf(newsId);
    }
}

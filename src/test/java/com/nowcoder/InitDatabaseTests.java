package com.nowcoder;

import com.nowcoder.dao.*;
import com.nowcoder.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    MessageDAO messageDAO;

    @Test
    public void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

            // 给每个资讯插入3个评论
            for (int j = 0; j < 3; ++j){
                Comment comment = new Comment();
                comment.setContent("this is a comment content" + j);
                comment.setCreatedDate(new Date());
                comment.setUserId(i+1);
                comment.setEntityType(EntityType.ENTITY_NEWS);// 为什么不用Comment
                comment.setEntityId(news.getId());
                commentDAO.addComment(comment);
            }

            for (int k = 0; k < 3; ++k) {
                Message message = new Message();
                message.setContent("hello"+i);
                message.setCreatedDate(new Date());
                message.setFromId(2);
                message.setToId(12);
                message.setHasRead(0);
               // message.set
                messageDAO.addMessage(message);
            }

            for (int k = 0; k < 2; ++k) {
                Message message = new Message();
                message.setContent("hello"+i);
                message.setCreatedDate(new Date());
                message.setFromId(2);
                message.setToId(3);
                message.setHasRead(0);
                // message.set
                messageDAO.addMessage(message);
            }

            for (int k = 0; k < 2; ++k) {
                Message message = new Message();
                message.setContent("hello"+i);
                message.setCreatedDate(new Date());
                message.setFromId(12);
                message.setToId(9);
                message.setHasRead(1);
                // message.set
                messageDAO.addMessage(message);
            }


            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i+1));
            loginTicketDAO.addTicket(ticket);

            loginTicketDAO.updateStatus(ticket.getTicket(), 2);

        }

        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));

        Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
        Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getStatus());

       // Assert.assertNotNull(commentDAO.selectByEntity(1, EntityType.ENTITY_NEWS).get(0));
    }

}

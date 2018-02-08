package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String messageList(Model model){
        int localuserId = hostHolder.getUser().getId();

        List<Message> messageList = messageService.getConversationList(localuserId, 0, 10);
        List<ViewObject> messages = new ArrayList<>();

        try {
            for (Message msg : messageList){
                ViewObject vo = new ViewObject();
                User user = userService.getUser(localuserId);

                vo.set("user", user);
                vo.set("unread", messageService.getConversationUnreadCount(msg.getConversationId(), localuserId));
                messages.add(vo);
                model.addAttribute(messages);
            }
        } catch (Exception e) {
            logger.error("添加消息失败："+ e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    public String message(@Param("fromId") int fromId,
                          @Param("toId") int toId,
                          @Param("content") String content){
       try {
            Message message = new Message();

            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(fromId);
            message.setToId(toId);
           // message.setConversationId(fromId > toId ? String.format("%d_%d" , fromId, toId):String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);

            return ToutiaoUtil.getJSONString(message.getId());

        } catch (Exception e) {
            logger.error("发送消息失败："+ e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发送消息失败");
        }

    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId){

        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();

            for (Message msg : messageList){
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user != null){
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);

        } catch (Exception e) {
            logger.error("读取消息失败"+e.getMessage());
        }

        return "letterDetail";
    }


}

package com.philia.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philia.model.Conversation;
import com.philia.model.MailBox;
import com.philia.model.Message;
import com.philia.service.IMessageService;

/**
 * 
 * @author khimung
 *
 */
@RestController
public class MessageController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @Resource
    private IMessageService messageService;

    /**
     * {@link /mails/{userId}}
     * 
     * @return
     */
    @RequestMapping(value = "/mails/{userId}", method = RequestMethod.GET)
    public MailBox mails(@PathVariable("userId") String userId) {
    	return messageService.getMailBox(userId);
    }
    
    /**
     * {@link /conversation/{userId}}
     * 
     * @return
     */
    @RequestMapping(value = "/conversation/{conversationId}", method = RequestMethod.GET)
    public Conversation conversation(@PathVariable("conversationId") String conversationId) {
    	return messageService.getConversation(conversationId);
    }
    
    /**
     * {@link /message
     *
     * @param message
     */
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public void message(@RequestBody Message message) {
    	//CompletableFuture.runAsync(() -> messageService.createMessage(message));
    	messageService.createMessage(message);
    }
    
}
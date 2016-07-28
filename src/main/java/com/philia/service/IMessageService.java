package com.philia.service;

import com.philia.model.Conversation;
import com.philia.model.MailBox;
import com.philia.model.Message;

public interface IMessageService {
	public void createMessage(Message message);
	public MailBox getMailBox(String userId);
	public Conversation getConversation(String conversationId);
}

package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.ConfirmMessageRead;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import io.reactivex.Observable;

public interface MessagesRepository {

  Observable<ChatMessage> sendMessage(ChatMessage message);

  Observable<ChatMessage> getMessagesObservable();

  Observable<ChatMessage> getSelectedMessageObservable();

  ChatMessage getSelectedMessage();

  ChatMessage getMessage(String messageId);

  ChatMessage getLastMessage();

  void informReadMessage(ConfirmMessageRead confirm);

  void putMessage(ChatMessage message);

  void selectMessage(ChatMessage message);
}


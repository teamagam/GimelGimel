package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entity.ConfirmMessageRead;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import io.reactivex.Observable;

public interface MessagesRepository {

  Observable<Message> sendMessage(Message message);

  Observable<Message> getMessagesObservable();

  Observable<Message> getSelectedMessageObservable();

  Message getSelectedMessage();

  Message getMessage(String messageId);

  Message getLastMessage();

  void informReadMessage(ConfirmMessageRead confirm);

  void putMessage(Message message);

  void selectMessage(Message message);
}


package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;

public interface MessageSender {

  Observable<ChatMessage> sendMessage(ChatMessage message);
}
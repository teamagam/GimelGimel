package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import io.reactivex.Observable;

public interface MessageSender {

  Observable<ChatMessage> sendMessage(OutGoingChatMessage message);
}
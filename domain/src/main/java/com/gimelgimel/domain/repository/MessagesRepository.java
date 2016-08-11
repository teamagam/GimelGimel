package com.gimelgimel.domain.repository;

import com.gimelgimel.domain.model.MessageModel;

public interface MessagesRepository {
    void sendMessage(MessageModel message);

    MessageModel getMessage();
}

package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Model's messages data interface
 */
public interface MessagesModel{
    int size();

    Message get(int index);

    int add(Message message);

    void remove(int index);

    int getReadCount();

    class AlreadyExistsException extends RuntimeException {
    }
}

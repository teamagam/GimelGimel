package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Model's messages data interface
 */
public interface MessagesModel extends DataChangedObservable {
    int size();

    Message get(int index);

    int add(Message message);

    void remove(int index);

    class AlreadyExistsException extends RuntimeException {
    }
}

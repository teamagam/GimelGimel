package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.message.viewModel.MessagesMasterViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Model's messages data interface
 */
public interface MessagesModel extends MessagesMasterViewModel.DisplayedMessagesRandomAccessor {
    int size();

    Message get(int index);

    int add(Message message);

    void remove(int index);

    void removeAll();

    class AlreadyExistsException extends RuntimeException {
    }
}

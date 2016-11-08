package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.message.viewModel.MessagesMasterViewModel;
import com.teamagam.gimelgimel.app.message.model.MessageApp;

/**
 * Model's messages data interface
 */
public interface MessagesModel extends MessagesMasterViewModel.DisplayedMessagesRandomAccessor {
    int size();

    MessageApp get(int index);

    void remove(int index);

    void removeAll();

    class AlreadyExistsException extends RuntimeException {
    }
}

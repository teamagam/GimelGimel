package com.teamagam.gimelgimel.app.network.services.message_polling.polling;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.Collection;

/**
 * Defines processing functionality for polled messages
 */
public interface IPolledMessagesProcessor {
    void process(Collection<Message> polledMessages);
}

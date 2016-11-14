package com.teamagam.gimelgimel.app.network.services.message_polling.polling;

import com.teamagam.gimelgimel.app.message.model.MessageApp;

import java.util.Collection;

/**
 * Defines processing functionality for polled messages
 */
public interface IPolledMessagesProcessor {
    void process(Collection<MessageApp> polledMessages);
}

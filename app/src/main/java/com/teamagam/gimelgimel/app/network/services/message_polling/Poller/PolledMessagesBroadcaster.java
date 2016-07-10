package com.teamagam.gimelgimel.app.network.services.message_polling.poller;

import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.Collection;

/**
 * Broadcast messages with injected {@link IMessageBroadcaster}
 */
public class PolledMessagesBroadcaster implements IPolledMessagesProcessor {

    private static final Logger sLogger = LoggerFactory.create(
            PolledMessagesBroadcaster.class);

    private IMessageBroadcaster mMessageBroadcaster;

    public PolledMessagesBroadcaster(IMessageBroadcaster messageBroadcaster) {
        mMessageBroadcaster = messageBroadcaster;
    }

    @Override
    public void process(Collection<Message> polledMessages) {
        if (polledMessages == null) {
            throw new IllegalArgumentException("polledMessages cannot be null");
        }

        sLogger.d("MessagePolling service processing " + polledMessages.size() + " new messages");

        for (Message msg : polledMessages) {
            mMessageBroadcaster.broadcast(msg);
        }
    }
}

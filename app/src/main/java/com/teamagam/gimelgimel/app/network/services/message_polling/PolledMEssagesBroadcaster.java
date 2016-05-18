package com.teamagam.gimelgimel.app.network.services.message_polling;

import android.util.Log;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;

import java.util.Collection;

/**
 * Broadcast messages with injected {@link IMessageBroadcaster}
 */
public class PolledMessagesBroadcaster implements IPolledMessagesProcessor {

    private static final String LOG_TAG = PolledMessagesBroadcaster.class.getSimpleName();

    private IMessageBroadcaster mMessageBroadcaster;

    public PolledMessagesBroadcaster(IMessageBroadcaster messageBroadcaster) {
        mMessageBroadcaster = messageBroadcaster;
    }

    @Override
    public void process(Collection<Message> polledMessages) {
        if (polledMessages == null) {
            throw new IllegalArgumentException("polledMessages cannot be null");
        }

        Log.v(LOG_TAG,
                "MessagePolling service processing " + polledMessages.size() + " new messages");

        for (Message msg :
                polledMessages) {
            mMessageBroadcaster.broadcast(msg);
        }
    }
}

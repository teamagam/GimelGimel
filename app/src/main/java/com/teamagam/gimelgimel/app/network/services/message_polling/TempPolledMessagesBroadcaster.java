package com.teamagam.gimelgimel.app.network.services.message_polling;

import android.util.Log;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.Collection;

/**
 * Broadcast messages with injected {@link IMessageBroadcaster}
 */
public class TempPolledMessagesBroadcaster implements IPolledMessagesProcessor {

    private static final String LOG_TAG = TempPolledMessagesBroadcaster.class.getSimpleName();

    private IMessageBroadcaster mMessageBroadcaster;

    public TempPolledMessagesBroadcaster(IMessageBroadcaster messageBroadcaster) {
        mMessageBroadcaster = messageBroadcaster;
    }

    @Override
    public void process(Collection<Message> polledMessages) {
        if (polledMessages == null) {
            throw new IllegalArgumentException("polledMessages cannot be null");
        }

        Log.d(LOG_TAG,
                "MessagePolling service processing " + polledMessages.size() + " new messages");

        for (Message msg : polledMessages) {
            mMessageBroadcaster.broadcast(msg);
        }
    }
}

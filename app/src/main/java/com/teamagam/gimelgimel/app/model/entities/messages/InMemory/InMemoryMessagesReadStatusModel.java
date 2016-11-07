package com.teamagam.gimelgimel.app.model.entities.messages.InMemory;

import com.teamagam.gimelgimel.app.common.NotifyingDataChangedObservable;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesReadStatusModel;

import java.util.HashSet;

/**
 * In-memory implementation of {@link MessagesReadStatusModel}
 * Meaning, all the read status data is kept for as long as the app lives
 */
public class InMemoryMessagesReadStatusModel extends NotifyingDataChangedObservable implements MessagesReadStatusModel {
    private HashSet<MessageApp> mReadMessages;


    public InMemoryMessagesReadStatusModel() {
        mReadMessages = new HashSet<>();
    }

    @Override
    public void markAsRead(MessageApp message) {
        mReadMessages.add(message);
        notifyObservers();
    }

    @Override
    public boolean isRead(MessageApp message) {
        return mReadMessages.contains(message);
    }

    @Override
    public int getCount() {
        return mReadMessages.size();
    }
}

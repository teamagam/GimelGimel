package com.teamagam.gimelgimel.app.model.entities.messages.InMemory;

import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;

import java.util.ArrayList;

/**
 * {@link MessagesModel} class that holds its messages in-memory
 */
public class InMemoryMessagesModel implements MessagesModel {

    private ArrayList<MessageApp> mMessages = new ArrayList<>();

    @Override
    public int size() {
        return mMessages.size();
    }

    @Override
    public MessageApp get(int index) {
        return mMessages.get(index);
    }

    @Override
    public void add(MessageApp message) {
        if (mMessages.contains(message)) {
            throw new AlreadyExistsException();
        }
        mMessages.add(message);

//        return mMessages.size() - 1;
    }

    @Override
    public int getPosition(String messageId) {

        return 0;
    }

    @Override
    public void remove(int index) {
        mMessages.remove(index);
    }

    @Override
    public void removeAll() {
        mMessages.clear();
    }
}

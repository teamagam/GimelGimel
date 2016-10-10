package com.teamagam.gimelgimel.app.model.entities.messages.InMemory;

import com.teamagam.gimelgimel.app.common.NotifyingDataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;

import java.util.ArrayList;

/**
 * {@link MessagesModel} class that holds its messages in-memory
 */
public class InMemoryMessagesModel implements MessagesModel {

    private ArrayList<Message> mMessages = new ArrayList<>();

    @Override
    public int size() {
        return mMessages.size();
    }

    @Override
    public Message get(int index) {
        return mMessages.get(index);
    }

    @Override
    public int add(Message message) {
        if (mMessages.contains(message)) {
            throw new AlreadyExistsException();
        }
        mMessages.add(message);

        return mMessages.size() - 1;
    }

    @Override
    public void remove(int index) {
        mMessages.remove(index);
    }
}

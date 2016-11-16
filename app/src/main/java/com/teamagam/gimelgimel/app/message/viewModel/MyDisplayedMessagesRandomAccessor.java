package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.message.model.MessageApp;

import java.util.HashMap;
import java.util.Map;

class MyDisplayedMessagesRandomAccessor implements MessagesMasterViewModel.DisplayedMessagesRandomAccessor {

    private Map<Integer, MessageApp> mMessagesByPosition;
    private Map<String, Integer> mPositionById;
    private int mMessageCount;


    public MyDisplayedMessagesRandomAccessor() {
        mMessagesByPosition = new HashMap<>();
        mPositionById = new HashMap<>();
        mMessageCount = 0;
    }

    @Override
    public void add(MessageApp messageApp) {
        if (mPositionById.containsKey(messageApp.getMessageId())) {
            throw new IllegalStateException("Message with the same ID cannot be added");
        }
        mMessagesByPosition.put(mMessageCount, messageApp);
        mPositionById.put(messageApp.getMessageId(), mMessageCount++);
    }

    @Override
    public int getPosition(String messageId) {
        Integer integer = mPositionById.get(messageId);
        return integer != null ? integer : -1;
    }

    @Override
    public int size() {
        return mMessageCount;
    }

    @Override
    public MessageApp get(int index) {
        return mMessagesByPosition.get(index);
    }
}

package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

public class MessagePresentation {

    private Message mMessage;
    private boolean mIsFromSelf;
    private boolean mIsShownOnMap;

    MessagePresentation(Message message, boolean isFromSelf, boolean isShownOnMap) {
        mMessage = message;
        mIsFromSelf = isFromSelf;
        mIsShownOnMap = isShownOnMap;
    }

    public Message getMessage() {
        return mMessage;
    }

    public boolean isFromSelf() {
        return mIsFromSelf;
    }

    public boolean isShownOnMap() {
        return mIsShownOnMap;
    }
}
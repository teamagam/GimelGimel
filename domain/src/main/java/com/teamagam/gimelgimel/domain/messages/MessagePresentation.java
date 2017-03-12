package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

public class MessagePresentation {

    private Message mMessage;
    private boolean mIsFromSelf;
    private boolean mIsShownOnMap;
    private boolean mIsRead;

    private MessagePresentation(Message message,
                                boolean isFromSelf,
                                boolean isShownOnMap,
                                boolean isRead) {
        mMessage = message;
        mIsFromSelf = isFromSelf;
        mIsShownOnMap = isShownOnMap;
        mIsRead = isRead;
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

    public boolean isRead() {
        return mIsRead;
    }

    static class Builder {
        private Message mMessage;
        private boolean mIsFromSelf;
        private boolean mIsShownOnMap;
        private boolean mIsRead;

        public Builder(Message message) {
            mMessage = message;
        }

        Builder setIsFromSelf(
                boolean isFromSelf) {
            mIsFromSelf = isFromSelf;
            return this;
        }

        Builder setIsShownOnMap(
                boolean isShownOnMap) {
            mIsShownOnMap = isShownOnMap;
            return this;
        }

        Builder setIsRead(
                boolean isRead) {
            mIsRead = isRead;
            return this;
        }

        MessagePresentation build() {
            return new MessagePresentation(mMessage, mIsFromSelf, mIsShownOnMap, mIsRead);
        }
    }
}
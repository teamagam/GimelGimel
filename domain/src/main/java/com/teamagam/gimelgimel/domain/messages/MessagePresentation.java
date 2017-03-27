package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

public class MessagePresentation {

    private Message mMessage;
    private boolean mIsFromSelf;
    private boolean mIsShownOnMap;
    private boolean mIsNotified;

    private MessagePresentation(Message message,
                                boolean isFromSelf,
                                boolean isShownOnMap,
                                boolean isNotified) {
        mMessage = message;
        mIsFromSelf = isFromSelf;
        mIsShownOnMap = isShownOnMap;
        mIsNotified = isNotified;
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

    public boolean isNotified() {
        return mIsNotified;
    }

    static class Builder {
        private Message mMessage;
        private boolean mIsFromSelf;
        private boolean mIsShownOnMap;
        private boolean mIsNotified;

        public Builder(Message message) {
            mMessage = message;
        }

        Builder setIsFromSelf(boolean isFromSelf) {
            mIsFromSelf = isFromSelf;
            return this;
        }

        Builder setIsShownOnMap(boolean isShownOnMap) {
            mIsShownOnMap = isShownOnMap;
            return this;
        }

        Builder setIsNotified(boolean isNotified) {
            mIsNotified = isNotified;
            return this;
        }

        MessagePresentation build() {
            return new MessagePresentation(mMessage, mIsFromSelf, mIsShownOnMap, mIsNotified);
        }
    }
}
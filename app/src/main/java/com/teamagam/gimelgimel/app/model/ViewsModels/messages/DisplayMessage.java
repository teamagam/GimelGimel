package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Data class for message displaying
 */
public class DisplayMessage {

    private Message mMessage;
    private boolean mIsSelected;
    private boolean mIsRead;

    public DisplayMessage(Message message, boolean isSelected, boolean isRead) {
        mMessage = message;
        mIsSelected = isSelected;
        mIsRead = isRead;
    }

    public Message getMessage() {
        return mMessage;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public boolean isRead() {
        return mIsRead;
    }

    void setRead() {
        mIsRead = true;
    }

    void setUnread() {
        mIsRead = false;
    }

    void setSelected() {
        mIsSelected = true;
    }

    void setUnselected() {
        mIsSelected = false;
    }

    public static class DisplayMessageBuilder {
        private Message message;
        private boolean isSelected = false;
        private boolean isRead = false;

        public DisplayMessageBuilder setMessage(Message message) {
            this.message = message;
            return this;
        }

        public DisplayMessageBuilder setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
            return this;
        }

        public DisplayMessageBuilder setIsRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public DisplayMessage build() {
            return new DisplayMessage(message,isSelected, isRead);
        }
    }
}

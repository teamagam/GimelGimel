package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.message.model.MessageApp;

/**
 * Data class for message displaying
 */
public class DisplayMessage {

    private MessageApp mMessage;
    private boolean mIsSelected;
    private boolean mIsRead;

    private DisplayMessage(MessageApp message, boolean isSelected, boolean isRead) {
        mMessage = message;
        mIsSelected = isSelected;
        mIsRead = isRead;
    }

    public MessageApp getMessage() {
        return mMessage;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public boolean isRead() {
        return mIsRead;
    }

    public static class DisplayMessageBuilder {
        private MessageApp message;
        private boolean isSelected = false;
        private boolean isRead = false;

        public DisplayMessageBuilder setMessage(MessageApp message) {
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

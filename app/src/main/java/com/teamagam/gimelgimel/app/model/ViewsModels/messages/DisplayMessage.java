package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Data class for message displaying
 */
public class DisplayMessage {

    private Message mMessage;
    private boolean mIsSelected;
    private boolean mIsRead;

    public DisplayMessage(Message message) {
        mMessage = message;
        mIsSelected = false;
        mIsRead = false;
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
}

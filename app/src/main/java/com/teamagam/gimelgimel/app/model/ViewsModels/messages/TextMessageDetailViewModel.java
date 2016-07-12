package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

/**
 * Text-messages content exposing
 */
public class TextMessageDetailViewModel extends MessageDetailViewModel {

    public TextMessageDetailViewModel(
            SelectedMessageModel selectedMessageModel) {
        super(selectedMessageModel);
    }

    public String getText() {
        validateSelectedMessage();
        return getTextMessageContent();
    }

    @Override
    protected String getExpectedMessageType() {
        return Message.TEXT;
    }

    private String getTextMessageContent() {
        return ((MessageText) mSelectedMessageModel.getSelected()).getContent();
    }
}

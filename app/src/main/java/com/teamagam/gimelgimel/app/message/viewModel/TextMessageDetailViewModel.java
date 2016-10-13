package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;

/**
 * Text-messages content exposing
 */
public class TextMessageDetailViewModel extends MessageDetailViewModel {

    public TextMessageDetailViewModel() {
        super();
    }

    public String getText() {
        return getTextMessageContent();
    }

    @Override
    protected String getExpectedMessageType() {
        return Message.TEXT;
    }

    private String getTextMessageContent() {
        return ((MessageText) mMessageSelected).getContent();
    }

    @Override
    protected void updateSelectedMessage() {

    }
}

package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailTextFragment;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;

import javax.inject.Inject;

/**
 * Text-messages content exposing
 */
public class TextMessageDetailViewModel extends MessageDetailViewModel<MessagesDetailTextFragment> {

    @Inject
    public TextMessageDetailViewModel() {
        super();
    }

    public String getText() {
        return getTextMessageContent();
    }

    @Override
    protected String getExpectedMessageType() {
        return MessageApp.TEXT;
    }

    private String getTextMessageContent() {
        return ((MessageTextApp) mMessageSelected).getContent();
    }

    @Override
    protected void updateSelectedMessage() {

    }
}

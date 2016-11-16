package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailTextFragment;

/**
 * Text-messages content exposing
 */
@AutoFactory
public class TextMessageDetailViewModel extends MessageDetailViewModel<MessagesDetailTextFragment> {

    private final MessageTextApp mTextMessage;

    public TextMessageDetailViewModel(
            @Provided Context context,
            MessageTextApp messageApp) {
        super(context, messageApp);
        mTextMessage = messageApp;
    }

    public String getText() {
        return mTextMessage.getContent();
    }
}

package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailTextFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import javax.inject.Inject;

/**
 * Text-messages content exposing
 */
public class TextMessageDetailViewModel extends MessageDetailViewModel<MessagesDetailTextFragment> {

    private MessageAppMapper mMessageAppMapper;
    private MessageTextApp mMessage;
    private String mTextContent;

    @Inject
    public TextMessageDetailViewModel(
            Context context,
            DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory,
            MessageAppMapper messageAppMapper) {
        super(context, selectedMessageInteractorFactory);

        mMessageAppMapper = messageAppMapper;
    }

    public String getText() {
        return mTextContent;
    }

    @Override
    protected MessageApp getMessage() {
        return mMessage;
    }

    @Override
    protected DisplaySelectedMessageInteractor.Displayer createDisplayer() {
        return new DisplaySelectedMessageInteractor.Displayer() {
            @Override
            public void display(Message message) {
                mMessage = (MessageTextApp) mMessageAppMapper.transformToModel(message);
                mTextContent = mMessage.getContent();

                notifyChange();
            }
        };
    }
}

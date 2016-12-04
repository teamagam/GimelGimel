package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailTextFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

/**
 * Text-messages content exposing
 */
@AutoFactory
public class TextMessageDetailViewModel extends MessageDetailViewModel<MessagesDetailTextFragment> {

    private MessageAppMapper mMessageAppMapper;
    private String mTextContent;

    public TextMessageDetailViewModel(
            @Provided Context context,
            @Provided DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory,
            @Provided MessageAppMapper messageAppMapper) {
        super(context, selectedMessageInteractorFactory);

        mMessageAppMapper = messageAppMapper;
    }

    @Override
    public void start() {
        super.start();

        createInteractor();
        mDisplaySelectedMessageInteractor.execute();
    }

    public String getText() {
        return mTextContent;
    }

    private void createInteractor() {
        mDisplaySelectedMessageInteractor =
                mDisplaySelectedMessageInteractorFactory.create(
                        new DisplaySelectedMessageInteractor.Displayer() {
                            @Override
                            public void display(Message message) {
                                mMessage = mMessageAppMapper.transformToModel(message);
                                mTextContent = (String) mMessage.getContent();

                                notifyChange();
                            }
                        });
    }
}

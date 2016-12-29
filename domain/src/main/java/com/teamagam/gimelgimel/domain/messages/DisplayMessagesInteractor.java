package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.Arrays;

@AutoFactory
public class DisplayMessagesInteractor extends BaseDisplayInteractor {

    private final Displayer mDisplayer;
    private final MessagesRepository mMessagesRepository;
    private final UserPreferencesRepository mUserPreferencesRepository;


    DisplayMessagesInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            @Provided UserPreferencesRepository userPreferencesRepository,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mMessagesRepository = messagesRepository;
        mUserPreferencesRepository = userPreferencesRepository;
        mDisplayer = displayer;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

        DisplaySubscriptionRequest displayMessages = factory.create(
                mMessagesRepository.getMessagesObservable(),
                this::showMessage);

        DisplaySubscriptionRequest displayRead = factory.create(
                mMessagesRepository.getReadMessagesObservable(),
                mDisplayer::read
        );

        DisplaySubscriptionRequest displaySelected = factory.create(
                mMessagesRepository.getSelectedMessageObservable(),
                mDisplayer::select);

        return Arrays.asList(displayMessages, displayRead, displaySelected);
    }

    private void showMessage(Message message) {
        mDisplayer.show(message, isMessageFromSelf(message));
    }

    private boolean isMessageFromSelf(Message message) {
        return message.getSenderId().equals(mUserPreferencesRepository.getPreference(
                Constants.USERNAME_PREFRENCE_KEY));
    }


    public interface Displayer {
        void show(Message message, boolean isFromSelf);

        void read(Message message);

        void select(Message message);
    }
}

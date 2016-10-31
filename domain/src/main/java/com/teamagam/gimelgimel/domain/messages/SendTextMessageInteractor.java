package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class SendTextMessageInteractor extends SendMessageInteractor<MessageText> {

    private String mText;

    protected SendTextMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided UserPreferencesRepository userPreferences,
            @Provided MessagesRepository messagesRepository,
            @Provided MessageNotifications messageNotifications,
            String text) {
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications);
        mText = text;
    }

    @Override
    protected MessageText createMessage(String senderId) {
        return new MessageText(null, senderId, null, false, false, mText);
    }
}

package com.teamagam.gimelgimel.domain.messages;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class SendUserLocationInteractor extends SendMessageInteractor<MessageUserLocation> {

    private LocationSampleEntity mLocationSample;

    public SendUserLocationInteractor(@Provided ThreadExecutor threadExecutor,
                                         @Provided UserPreferencesRepository userPreferences,
                                         @Provided MessagesRepository messagesRepository,
                                         @Provided MessageNotifications messageNotifications,
                                         LocationSampleEntity locationSampleEntity) {
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications);

        mLocationSample = locationSampleEntity;
    }

    @Override
    protected MessageUserLocation createMessage(String senderId) {
        return new MessageUserLocation(null, senderId, null, false, false, mLocationSample);
    }
}

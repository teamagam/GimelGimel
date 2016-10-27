package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

@AutoFactory
public class SendImageMessageInteractor extends CreateMessageInteractor<MessageImage> {

    private static final String IMAGE_SOURCE = "User";

    private final MessagesRepository mMessagesRepository;
    private final ImagesRepository mImagesRepository;
    private final LocationRepository mLocationRepository;
    private final MessageNotifications mMessageNotifications;
    private long mImageTime;

    public SendImageMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided UserPreferencesRepository userPreferences,
            @Provided MessagesRepository messagesRepository,
            @Provided ImagesRepository imagesRepository,
            @Provided LocationRepository locationRepository,
            @Provided MessageNotifications messageNotifications,
            long imageTime) {
        super(threadExecutor, userPreferences);
        mImagesRepository = imagesRepository;
        mMessagesRepository = messagesRepository;
        mLocationRepository = locationRepository;
        mMessageNotifications = messageNotifications;
        mImageTime = imageTime;
    }

    @Override
    protected MessageImage createMessage(String senderId) {
        PointGeometry lastLocation = mLocationRepository.getLastLocationSample().getLocation();
        ImageMetadata imageMetadata = new ImageMetadata(mImageTime, mImagesRepository.getImagePath(),
                lastLocation, IMAGE_SOURCE);
        return new MessageImage(senderId, imageMetadata);
    }

    @Override
    protected Observable<MessageImage> buildUseCaseObservable() {
        return super.buildUseCaseObservable()
                .doOnNext(mMessageNotifications::sending)
                .flatMap(m -> mImagesRepository.uploadImage(m, mImagesRepository.getImagePath()))
                .doOnNext(mMessagesRepository::putMessage)
                .doOnError(t -> {
                    if (getMessage() != null) {
                        mMessageNotifications.error(getMessage());
                    }
                })
                .doOnNext(mMessageNotifications::success);
    }
}

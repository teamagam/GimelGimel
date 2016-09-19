package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

@AutoFactory
public class SendImageMessageInteractor extends CreateMessageInteractor<MessageImage> {

    private static final String IMAGE_SOURCE = "User";

    private final MessagesRepository mMessagesRepository;
    private final ImagesRepository mImagesRepository;
    private final LocationRepository mLocationRepository;
    private final String mImagePath;
    private long mImageTime;

    public SendImageMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided UserPreferencesRepository userPreferences,
            @Provided MessagesRepository messagesRepository,
            @Provided ImagesRepository imagesRepository,
            @Provided LocationRepository locationRepository,
            String imagePath,
            long imageTime) {
        super(threadExecutor, postExecutionThread, userPreferences);
        mImagesRepository = imagesRepository;
        mMessagesRepository = messagesRepository;
        mLocationRepository = locationRepository;
        mImagePath = imagePath;
        mImageTime = imageTime;
    }

    @Override
    protected MessageImage createMessage(String senderId) {
        PointGeometry lastLocation = mLocationRepository.getLocation();
        ImageMetadata imageMetadata = new ImageMetadata(mImageTime, mImagePath, lastLocation,
                IMAGE_SOURCE);
        return new MessageImage(senderId, imageMetadata);
    }

    @Override
    protected Observable<MessageImage> buildUseCaseObservable() {
        return super.buildUseCaseObservable()
                .flatMap(m -> mImagesRepository.uploadImage(m, mImagePath))
                .doOnNext(mMessagesRepository::putMessage);
    }
}

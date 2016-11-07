package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

@AutoFactory
public class SendImageMessageInteractor extends SendBaseGeoMessageInteractor<MessageImage> {

    private static final String IMAGE_SOURCE = "User";

    private final ImagesRepository mImagesRepository;
    private final LocationRepository mLocationRepository;
    private long mImageTime;
    private PointGeometry mLocation;


    public SendImageMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided UserPreferencesRepository userPreferences,
            @Provided MessagesRepository messagesRepository,
            @Provided ImagesRepository imagesRepository,
            @Provided LocationRepository locationRepository,
            @Provided MessageNotifications messageNotifications,
            @Provided GeoEntitiesRepository geoEntitiesRepository,
            long imageTime) {
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications, geoEntitiesRepository);
        mImagesRepository = imagesRepository;
        mLocationRepository = locationRepository;
        mImageTime = imageTime;
    }

    @Override
    protected MessageImage createMessage(String senderId) {
        ImageMetadata imageMetadata = new ImageMetadata(mImageTime, mImagesRepository.getImagePath(),
                mEntityId, IMAGE_SOURCE);
        return new MessageImage(null, senderId, null, false, false, imageMetadata);
    }

    @Override
    protected Observable<MessageImage> buildUseCaseObservable() {
        mLocation = mLocationRepository.getLocation();
        Observable<MessageImage> sendImageObservable = super.buildUseCaseObservable();
        if (mLocation != null) {
            return storeGeoEntityObservable()
                    .flatMap(e -> sendImageObservable);
        } else {
            return sendImageObservable;
        }
    }

    @Override
    protected GeoEntity createGeoEntity(String messageId) {
        return new ImageEntity(messageId, mLocation);
    }
}

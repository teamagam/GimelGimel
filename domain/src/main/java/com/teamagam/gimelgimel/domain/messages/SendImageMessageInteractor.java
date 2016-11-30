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
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

@AutoFactory
public class SendImageMessageInteractor extends SendBaseGeoMessageInteractor<MessageImage> {

    private static final String IMAGE_SOURCE = "User";

    private final LocationRepository mLocationRepository;
    private long mImageTime;
    private String mLocalUrl;
    private PointGeometry mLocation;
    private GeoEntity mGeoEntity;


    public SendImageMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided UserPreferencesRepository userPreferences,
            @Provided MessagesRepository messagesRepository,
            @Provided LocationRepository locationRepository,
            @Provided MessageNotifications messageNotifications,
            @Provided GeoEntitiesRepository geoEntitiesRepository,
            long imageTime,
            String localUrl) {
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications,
                geoEntitiesRepository);
        mLocationRepository = locationRepository;
        mImageTime = imageTime;
        mLocalUrl = localUrl;
    }

    @Override
    protected MessageImage createMessage(String senderId) {
        ImageMetadata imageMetadata = new ImageMetadata(mImageTime,
                null, mLocalUrl, mGeoEntity, IMAGE_SOURCE);
        return new MessageImage(null, senderId, null, imageMetadata);
    }

    @Override
    protected Observable<MessageImage> buildUseCaseObservable() {
        return Observable.just(mLocationRepository)
                .map(LocationRepository::getLastLocationSample)
                .map(LocationSample::getLocation)
                .flatMap(location -> {
                    mLocation = location;
                    if (location == null) {
                        return super.buildUseCaseObservable();
                    } else {
                        return storeGeoEntityObservable()
                                .doOnNext(geoEntity -> mGeoEntity = geoEntity)
                                .flatMap(e -> super.buildUseCaseObservable());
                    }
                });
    }

    @Override
    protected GeoEntity createGeoEntity(String messageId) {
        return new ImageEntity(messageId, null, mLocation);
    }
}

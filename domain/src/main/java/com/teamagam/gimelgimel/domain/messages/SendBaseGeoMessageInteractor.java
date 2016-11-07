package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

/**
 * Created on 11/7/2016.
  */
abstract class SendBaseGeoMessageInteractor<T extends Message> extends
        SendMessageInteractor<T>{

    private static final String LAYER_ID = "SentMessagesLayer";

    private final UserPreferencesRepository mUserPreferences;
    private final GeoEntitiesRepository mGeoEntitiesRepository;
    protected String mEntityId;

    SendBaseGeoMessageInteractor(
            ThreadExecutor threadExecutor,
            UserPreferencesRepository userPreferences,
            MessagesRepository messagesRepository,
            MessageNotifications messageNotifications,
            GeoEntitiesRepository geoEntitiesRepository){
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications);
        mUserPreferences = userPreferences;
        mGeoEntitiesRepository = geoEntitiesRepository;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.just(mUserPreferences)
                .map(this::createGeoEntity)
                .doOnNext(this::storeLocallyEntityId)
                .doOnNext(entity -> entity.setLayerTag(LAYER_ID))
                .doOnNext(mGeoEntitiesRepository::add)
                .doOnNext(this::showEntityIfNeeded)
                .flatMap(geoEntity ->
                        super.buildUseCaseObservable()
                );
    }

    private void storeLocallyEntityId(GeoEntity geoEntity) {
        mEntityId = geoEntity.getId();
    }

    protected abstract void showEntityIfNeeded(GeoEntity geoEntity);

    protected abstract GeoEntity createGeoEntity(UserPreferencesRepository userPreferences);

}

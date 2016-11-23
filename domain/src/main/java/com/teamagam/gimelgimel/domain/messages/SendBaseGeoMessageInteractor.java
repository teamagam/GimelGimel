package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import com.teamagam.gimelgimel.domain.utils.IdCreatorUtil;

import rx.Observable;

/**
 * Created on 11/7/2016.
  */
abstract class SendBaseGeoMessageInteractor<T extends Message> extends
        SendMessageInteractor<T>{

    private static final String LAYER_ID = "SentMessagesLayer";

    private final UserPreferencesRepository mUserPreferences;
    private final GeoEntitiesRepository mGeoEntitiesRepository;

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

    protected Observable<GeoEntity> storeGeoEntityObservable(){
        return Observable.just(mUserPreferences)
                .map(this::getEntityId)
                .map(this::createGeoEntity)
                .filter(geoEntity -> geoEntity != null)
                .doOnNext(entity -> entity.setLayerTag(LAYER_ID))
                .doOnNext(mGeoEntitiesRepository::add);
    }

    protected abstract GeoEntity createGeoEntity(String id);

    private String getEntityId(UserPreferencesRepository userPreferences) {
        return userPreferences.getPreference(Constants.USERNAME_PREFRENCE_KEY) + ":" + IdCreatorUtil.getUniqueId();
    }
}

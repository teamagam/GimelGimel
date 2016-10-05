package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.BaseGeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.Symbol;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.concurrent.TimeUnit;

import rx.Observable;

@AutoFactory
public class SendGeoMessageInteractor extends SendMessageInteractor<MessageGeo> {

    private static final String LAYER_ID = "SentMessagesLayer";

    private final GeoEntitiesRepository mGeoEntitiesRepository;
    private final DisplayedEntitiesRepository mGeoDisplayedRepo;
    private final String mMessageText;
    private final PointGeometry mMessageGeometry;
    private final String mMessageType;

    SendGeoMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided UserPreferencesRepository userPreferences,
            @Provided MessagesRepository messagesRepository,
            @Provided MessageNotifications messageNotifications,
            @Provided GeoEntitiesRepository geoEntitiesRepository,
            @Provided DisplayedEntitiesRepository geoDisplayedRepo,
            String text,
            PointGeometry pg,
            String type) {
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications);
        mGeoEntitiesRepository = geoEntitiesRepository;
        mGeoDisplayedRepo = geoDisplayedRepo;
        mMessageText = text;
        mMessageGeometry = pg;
        mMessageType = type;
    }

    @Override
    protected Observable<MessageGeo> buildUseCaseObservable() {
        return super.buildUseCaseObservable()
                .doOnNext(messageGeo ->
                        mGeoEntitiesRepository.add(messageGeo.getGeoEntity()))
                .doOnNext(messageGeo ->
                        mGeoDisplayedRepo.show(messageGeo.getGeoEntity()));
    }

    @Override
    protected MessageGeo createMessage(String senderId) {
        GeoEntity geoEntity = createGeoEntity(senderId + mMessageText + mMessageType,
                mMessageGeometry,
                mMessageType);
        return new MessageGeo(senderId, geoEntity, mMessageText, mMessageType);
    }

    private GeoEntity createGeoEntity(String id, Geometry geometry, String type) {
        Symbol symbol = createSymbolFromType(type);

        return new BaseGeoEntity(id, geometry, symbol, LAYER_ID);
    }

    private Symbol createSymbolFromType(String type) {
        // TODO: define symbols models and create them by the mMessageType
        return null;
    }
}

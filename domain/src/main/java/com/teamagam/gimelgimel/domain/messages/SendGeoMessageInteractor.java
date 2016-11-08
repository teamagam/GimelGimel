package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

@AutoFactory
public class SendGeoMessageInteractor extends SendBaseGeoMessageInteractor<MessageGeo> {

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
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications,
                geoEntitiesRepository);
        mGeoDisplayedRepo = geoDisplayedRepo;
        mMessageText = text;
        mMessageGeometry = pg;
        mMessageType = type;
    }

    @Override
    protected Observable<MessageGeo> buildUseCaseObservable() {
        return storeGeoEntityObservable()
                .doOnNext(mGeoDisplayedRepo::show)
                .flatMap(e -> super.buildUseCaseObservable());
    }

    @Override
    protected MessageGeo createMessage(String senderId) {
        return new MessageGeo(null, senderId, null, mEntityId);
    }

    @Override
    protected GeoEntity createGeoEntity(String id) {
        PointSymbol symbol = createSymbolFromType(mMessageType);
        return new PointEntity(id, mMessageText, mMessageGeometry, symbol);
    }

    private PointSymbol createSymbolFromType(String type) {
        return new PointSymbol(type);
    }

}

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
import com.teamagam.gimelgimel.domain.utils.IdCreatorUtil;

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
                .doOnNext(msg ->
                        msg.getGeoEntity().setLayerTag(LAYER_ID))
                .doOnNext(messageGeo ->
                        mGeoEntitiesRepository.add(messageGeo.getGeoEntity()))
                .doOnNext(messageGeo ->
                        mGeoDisplayedRepo.show(messageGeo.getGeoEntity()));
    }

    @Override
    protected MessageGeo createMessage(String senderId) {
        PointSymbol symbol = createSymbolFromType(mMessageType);
        GeoEntity geoEntity = createGeoEntity(senderId + ":" + IdCreatorUtil.getUniqueId(),
                mMessageGeometry,
                symbol);
        return new MessageGeo(senderId, geoEntity, mMessageText, mMessageType);
    }

    private GeoEntity createGeoEntity(String id, PointGeometry geometry, PointSymbol symbol) {
        return new PointEntity(id, LAYER_ID, geometry, symbol);
    }

    private PointSymbol createSymbolFromType(String type) {
        return new PointSymbol(type);
    }
}

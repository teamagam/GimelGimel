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
        return super.buildUseCaseObservable();
    }

    @Override
    protected void showEntityIfNeeded(GeoEntity geoEntity) {
        mGeoDisplayedRepo.show(geoEntity);
    }

    @Override
    protected MessageGeo createMessage(String senderId) {
        return new MessageGeo(null, senderId, null, false, false, mEntityId,
                mMessageText);
    }

    @Override
    protected GeoEntity createGeoEntity(UserPreferencesRepository userPreferences) {
        String id = userPreferences.getSenderId() + ":" + IdCreatorUtil.getUniqueId();
        PointSymbol symbol = createSymbolFromType(mMessageType);
        return new PointEntity(id, mMessageGeometry, symbol);
    }

    private PointSymbol createSymbolFromType(String type) {
        return new PointSymbol(type, null);
    }

}

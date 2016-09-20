package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.geometries.entities.BaseGeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.geometries.entities.Symbol;
import com.teamagam.gimelgimel.domain.geometries.repository.GeoEntityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

@AutoFactory
public class SendGeoMessageInteractor extends SendMessageInteractor<MessageGeo> {

    private static final String LAYER_ID = "SentMessagesLayer";

    private final GeoEntityRepository mGeoEntityRepository;
    private final String mMessageText;
    private final PointGeometry mMessageGeometry;
    private final String mMessageType;

    protected SendGeoMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided UserPreferencesRepository userPreferences,
            @Provided MessagesRepository messagesRepository,
            @Provided GeoEntityRepository geoEntityRepository,
            @Provided MessageNotifications messageNotifications,
            String text,
            PointGeometry pg,
            String type) {
        super(threadExecutor, userPreferences, messagesRepository, messageNotifications);
        mGeoEntityRepository = geoEntityRepository;
        mMessageText = text;
        mMessageGeometry = pg;
        mMessageType = type;
    }

    @Override
    protected Observable<MessageGeo> buildUseCaseObservable() {
        return super.buildUseCaseObservable()
                .doOnNext(message -> mGeoEntityRepository.addGeoEntityToVectorLayer(LAYER_ID,
                        message.getGeoEntity()));
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

        return new BaseGeoEntity(id, geometry, symbol);
    }

    private Symbol createSymbolFromType(String type) {
        // TODO: define symbols models and create them by the mMessageType
        return null;
    }
}

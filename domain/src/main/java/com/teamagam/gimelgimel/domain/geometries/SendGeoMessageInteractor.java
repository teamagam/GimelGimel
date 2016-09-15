package com.teamagam.gimelgimel.domain.geometries;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.geometries.entities.BaseGeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.geometries.entities.Symbol;
import com.teamagam.gimelgimel.domain.geometries.repository.GeoEntityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.interfaces.UserPreferences;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;

public class SendGeoMessageInteractor extends AbstractInteractor<MessageGeo> {

    private static final String LAYER_ID = "SentMessagesLayer";

    private final UserPreferences mPreferences;
    private GeoEntityRepository mGeoEntityRepository;
    private MessagesRepository mMessagesRepository;
    private MessageGeo mMessage;

    public SendGeoMessageInteractor(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    GeoEntityRepository repository,
                                    MessagesRepository messagesRepository, UserPreferences pref) {
        super(threadExecutor, postExecutionThread);

        mPreferences = pref;
        mGeoEntityRepository = repository;
        mMessagesRepository = messagesRepository;
    }

    public void sendGeoMessageEntity(Subscriber<MessageGeo> subscriber, String messageText,
                                     PointGeometry geometry, String type) {
        String username = mPreferences.getSenderName();
        GeoEntity geoEntity = createGeoEntity(username + messageText + type, geometry, type);
        MessageGeo message = new MessageGeo(username, geoEntity, messageText, type);

        sendGeoMessageEntity(message, subscriber);

    }

    @Override
    protected Observable<MessageGeo> buildUseCaseObservable() {
        if (mMessage == null || mMessage.getGeoEntity() == null) {
            throw new IllegalArgumentException(
                    "One of the following parameters is null: MessageGeo, MessageGeo.geoEntity");
        }

        return mMessagesRepository.sendMessage(mMessage)
                .map(message -> (MessageGeo) message)
                .doOnNext(message -> mMessagesRepository.putMessage(message))
                .doOnNext(message ->
                        mGeoEntityRepository.addGeoEntityToVectorLayer(LAYER_ID,
                                message.getGeoEntity()));
    }

    private GeoEntity createGeoEntity(String id, Geometry geometry, String type) {
        Symbol symbol = createSymbolFromType(type);

        return new BaseGeoEntity(id, geometry, symbol);
    }

    private Symbol createSymbolFromType(String type) {
        // TODO: define symbols models and create them by the type
        return null;
    }

    private void sendGeoMessageEntity(MessageGeo message, Subscriber<MessageGeo> subscriber) {
        mMessage = message;

        execute(subscriber);
    }
}

package com.teamagam.gimelgimel.domain.geometries;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.geometries.repository.GeoEntityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;

public class SendGeoMessageInteractor extends AbstractInteractor {

    private static final String LAYER_ID = "SentMessagesLayer";

    private GeoEntityRepository mGeoEntityRepository;
    private MessagesRepository mMessagesRepository;
    private MessageGeo mMessage;

    public SendGeoMessageInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                    GeoEntityRepository repository, MessagesRepository messagesRepository) {
        super(threadExecutor, postExecutionThread);

        mGeoEntityRepository = repository;
        mMessagesRepository = messagesRepository;
    }

    public void sendGeoMessageEntity(MessageGeo message, Subscriber subscriber) {
        mMessage = message;

        execute(subscriber);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (mMessage == null || mMessage.getGeoEntity() == null) {
            throw new IllegalArgumentException("One of the following parameters is null: MessageGeo, MessageGeo.geoEntity");
        }

        return mMessagesRepository.sendMessage(mMessage)
                .map(message -> (MessageGeo) message)
                .doOnNext(message -> mMessagesRepository.putMessage(message))
                .doOnNext(message ->
                        mGeoEntityRepository.addGeoEntityToVectorLayer(LAYER_ID,
                                message.getGeoEntity()));
    }

}

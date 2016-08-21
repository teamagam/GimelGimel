package com.teamagam.gimelgimel.domain.geometries;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.geometries.entities.VectorLayer;
import com.teamagam.gimelgimel.domain.geometries.repository.GeoRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;

public class GeometryInteractor extends AbstractInteractor {

    private static final String LAYER_ID = "UserLayer";

    private GeoRepository mGeoRepository;
    private MessagesRepository mMessagesRepository;
    private GeoEntity mGeoEntity;
    private MessageGeo mMessage;

    protected GeometryInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                 GeoRepository repository, MessagesRepository messagesRepository) {
        super(threadExecutor, postExecutionThread);

        mGeoRepository = repository;
        mMessagesRepository = messagesRepository;
    }

    public void addUserGeoEntity(GeoEntity entity, MessageGeo message, Subscriber subscriber) {
        mGeoEntity = entity;
        mMessage = message;

        execute(subscriber);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (mGeoEntity == null || mMessage == null) {
            throw new IllegalArgumentException("One of the following parameters is null: entity, message");
        }

        return mMessagesRepository.sendMessage(mMessage)
                .doOnNext(mMessagesRepository::putMessage)
                .doOnCompleted(() ->
                        mGeoRepository.getVectorLayerById(LAYER_ID)
                                .count()
                                .subscribe(count -> {
                                    if (count == 0) {
                                        mGeoRepository.addVectorLayer(createUserVectorLayer());
                                    }

                                    mGeoRepository.addGeoEntityToVectorLayer(LAYER_ID, mGeoEntity);
                                })
                );
    }

    private VectorLayer createUserVectorLayer() {
        return new VectorLayer(LAYER_ID);
    }
}

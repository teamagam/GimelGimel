package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.Arrays;

import rx.Observable;

@AutoFactory
public class SelectEntityInteractor extends BaseDataInteractor {

    private final EntityMessageMapper mEntityMessageMapper;
    private final MessagesRepository mMessagesRepository;
    private final GeoEntitiesRepository mGeoEntitiesRepository;
    private final SelectedEntityRepository mSelectedEntityRepository;
    private final String mEntityId;

    protected SelectEntityInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided EntityMessageMapper entityMessageMapper,
            @Provided MessagesRepository messagesRepository,
            @Provided GeoEntitiesRepository geoEntitiesRepository,
            @Provided SelectedEntityRepository selectedEntityRepository,
            String entityId) {
        super(threadExecutor);
        mEntityMessageMapper = entityMessageMapper;
        mMessagesRepository = messagesRepository;
        mGeoEntitiesRepository = geoEntitiesRepository;
        mSelectedEntityRepository = selectedEntityRepository;
        mEntityId = entityId;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return Arrays.asList(
                buildSelectMessageRequest(factory),
                buildSelectEntityRequest(factory)
        );
    }

    private DataSubscriptionRequest buildSelectMessageRequest(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return factory.create(
                Observable.just(mEntityId)
                        .flatMap(mEntityMessageMapper::getMessageId)
                        .flatMap(mMessagesRepository::getMessage)
                        .filter(m -> m != null)
                        .doOnNext(mMessagesRepository::selectMessage));
    }

    private DataSubscriptionRequest buildSelectEntityRequest(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return factory.create(
                Observable.just(mEntityId)
                        .map(mGeoEntitiesRepository::get)
                        .doOnNext(this::updateSelectedEntity)
        );
    }

    private void updateSelectedEntity(GeoEntity geoEntity) {
        if (isReselection(geoEntity)) {
            removeOldSelection();
        } else {
            removeOldSelection();
            updateSelection(geoEntity);
        }
    }

    private boolean isReselection(GeoEntity geoEntity) {
        return geoEntity.getId().equals(mSelectedEntityRepository.getSelectedEntityId());
    }

    private void removeOldSelection() {
        String oldSelectionId = mSelectedEntityRepository.getSelectedEntityId();
        if (oldSelectionId == null) {
            return;
        }
        mGeoEntitiesRepository.update(
                createSelectedModifiedGeoEntity(mGeoEntitiesRepository.get(oldSelectionId), false));
        mSelectedEntityRepository.setSelected(null);
    }

    private GeoEntity createSelectedModifiedGeoEntity(GeoEntity geoEntity, boolean isSelected) {
        GeoEntityDuplicateVisitor geoEntityDuplicateVisitor =
                new GeoEntityDuplicateVisitor(isSelected);
        geoEntity.accept(geoEntityDuplicateVisitor);
        return geoEntityDuplicateVisitor.getResult();
    }

    private void updateSelection(GeoEntity geoEntity) {
        mGeoEntitiesRepository.update(createSelectedModifiedGeoEntity(geoEntity, true));
        mSelectedEntityRepository.setSelected(geoEntity.getId());
    }

    private class GeoEntityDuplicateVisitor implements IGeoEntityVisitor {

        private final boolean mNewSelectedValue;
        private GeoEntity mResult;

        GeoEntityDuplicateVisitor(boolean newSelectedValue) {
            mNewSelectedValue = newSelectedValue;
        }

        @Override
        public void visit(PointEntity entity) {
            PointSymbol newSymbol = new PointSymbol(mNewSelectedValue,
                    entity.getSymbol().getType());
            mResult = new PointEntity(
                    entity.getId(),
                    entity.getText(),
                    entity.getGeometry(),
                    newSymbol);
        }

        @Override
        public void visit(ImageEntity entity) {
            mResult = new ImageEntity(
                    entity.getId(),
                    entity.getText(),
                    entity.getGeometry(),
                    mNewSelectedValue
            );
        }

        @Override
        public void visit(UserEntity entity) {
            UserSymbol newSymbol = entity.getSymbol().isActive() ?
                    UserSymbol.createActive(entity.getSymbol().getUserName(), mNewSelectedValue) :
                    UserSymbol.createStale(entity.getSymbol().getUserName(), mNewSelectedValue);

            mResult = new UserEntity(
                    entity.getId(),
                    entity.getText(),
                    entity.getGeometry(),
                    newSymbol
            );
        }

        @Override
        public void visit(MyLocationEntity entity) {
            MyLocationSymbol newSymbol = new MyLocationSymbol(mNewSelectedValue);
            mResult = new MyLocationEntity(
                    entity.getId(),
                    entity.getText(),
                    newSymbol,
                    entity.getGeometry()
            );
        }

        @Override
        public void visit(SensorEntity entity) {
            mResult = new SensorEntity(
                    entity.getId(),
                    entity.getText(),
                    entity.getGeometry(),
                    mNewSelectedValue
            );
        }

        @Override
        public void visit(AlertEntity alertEntity) {
            mResult = new AlertEntity(
                    alertEntity.getId(),
                    alertEntity.getText(),
                    alertEntity.getGeometry(),
                    alertEntity.getSymbol().getSeverity(),
                    mNewSelectedValue
            );
        }

        GeoEntity getResult() {
            return mResult;
        }
    }
}
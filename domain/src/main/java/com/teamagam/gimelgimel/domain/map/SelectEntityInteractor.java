package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import io.reactivex.Observable;

@AutoFactory
public class SelectEntityInteractor extends BaseSingleDataInteractor {

  private final GeoEntitiesRepository mGeoEntitiesRepository;
  private final SelectedEntityRepository mSelectedEntityRepository;
  private final String mEntityId;

  protected SelectEntityInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided GeoEntitiesRepository geoEntitiesRepository,
      @Provided SelectedEntityRepository selectedEntityRepository,
      String entityId) {
    super(threadExecutor);
    mGeoEntitiesRepository = geoEntitiesRepository;
    mSelectedEntityRepository = selectedEntityRepository;
    mEntityId = entityId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mEntityId),
        entityIdObservable -> entityIdObservable.map(mGeoEntitiesRepository::get)
            .filter(x -> x != null)
            .doOnNext(this::updateSelectedEntity));
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
    if (!mSelectedEntityRepository.isSelected()) {
      return;
    }
    String oldSelectionId = mSelectedEntityRepository.getSelectedEntityId();
    mGeoEntitiesRepository.update(
        createSelectedModifiedGeoEntity(mGeoEntitiesRepository.get(oldSelectionId), false));
    mSelectedEntityRepository.deselect();
  }

  private GeoEntity createSelectedModifiedGeoEntity(GeoEntity geoEntity, boolean isSelected) {
    GeoEntityDuplicateVisitor geoEntityDuplicateVisitor = new GeoEntityDuplicateVisitor(isSelected);
    geoEntity.accept(geoEntityDuplicateVisitor);
    return geoEntityDuplicateVisitor.getResult();
  }

  private void updateSelection(GeoEntity geoEntity) {
    mGeoEntitiesRepository.update(createSelectedModifiedGeoEntity(geoEntity, true));
    mSelectedEntityRepository.setSelected(geoEntity.getId());
  }

  private class GeoEntityDuplicateVisitor implements GeoEntityVisitor {

    private final boolean mNewSelectedValue;
    private GeoEntity mResult;

    GeoEntityDuplicateVisitor(boolean newSelectedValue) {
      mNewSelectedValue = newSelectedValue;
    }

    @Override
    public void visit(ImageEntity entity) {
      mResult = new ImageEntity(entity.getId(), entity.getGeometry(), mNewSelectedValue);
    }

    @Override
    public void visit(UserEntity entity) {
      UserSymbol newSymbol =
          entity.getSymbol().isActive() ? UserSymbol.createActive(entity.getSymbol().getUserName(),
              mNewSelectedValue)
              : UserSymbol.createStale(entity.getSymbol().getUserName(), mNewSelectedValue);

      mResult = new UserEntity(entity.getId(), entity.getGeometry(), newSymbol);
    }

    @Override
    public void visit(AlertPointEntity entity) {
      AlertPointSymbol newSymbol = new AlertPointSymbol(mNewSelectedValue);
      mResult = new AlertPointEntity(entity.getId(), entity.getSeverity(), entity.getGeometry(),
          newSymbol);
    }

    @Override
    public void visit(AlertPolygonEntity entity) {
      AlertPolygonSymbol newSymbol = new AlertPolygonSymbol(mNewSelectedValue);
      mResult = new AlertPolygonEntity(entity.getId(), entity.getSeverity(), entity.getGeometry(),
          newSymbol);
    }

    @Override
    public void visit(PointEntity entity) {
      PointSymbol newSymbol = new PointSymbol.PointSymbolBuilder().copy(entity.getSymbol())
          .setIsSelected(mNewSelectedValue)
          .build();
      mResult = new PointEntity(entity.getId(), entity.getGeometry(), newSymbol);
    }

    @Override
    public void visit(PolylineEntity entity) {
      PolylineSymbol selectedSymbol =
          new PolylineSymbol.PolylineSymbolBuilder().copy(entity.getSymbol())
              .setIsSelected(mNewSelectedValue)
              .build();
      mResult = new PolylineEntity(entity.getId(), entity.getGeometry(), selectedSymbol);
    }

    @Override
    public void visit(PolygonEntity entity) {
      PolygonSymbol selectedSymbol =
          new PolygonSymbol.PolygonSymbolBuilder().copy(entity.getSymbol())
              .setIsSelected(mNewSelectedValue)
              .build();
      mResult = new PolygonEntity(entity.getId(), entity.getGeometry(), selectedSymbol);
    }

    GeoEntity getResult() {
      return mResult;
    }
  }
}
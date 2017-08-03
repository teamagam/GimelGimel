package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeometryVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoFactory
public class OnDynamicLayerListingClickInteractor extends BaseSingleDataInteractor {

  private final DynamicLayerVisibilityRepository mDynamicLayerVisibilityRepository;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final DynamicLayerEnvelopeExtractor mDynamicLayerEnvelopeExtractor;
  private final DynamicLayer mDynamicLayer;

  protected OnDynamicLayerListingClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerVisibilityRepository dynamicLayerVisibilityRepository,
      @Provided
          com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      DynamicLayer dynamicLayer) {
    super(threadExecutor);
    mDynamicLayerVisibilityRepository = dynamicLayerVisibilityRepository;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mDynamicLayerEnvelopeExtractor = new DynamicLayerEnvelopeExtractor();
    mDynamicLayer = dynamicLayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayer),
        dlObservable -> dlObservable.map(this::getNewVisibilityState)
            .doOnNext(this::setNewVisibility)
            .filter(isShown -> isShown)
            .filter(flag -> !mDynamicLayer.getEntities().isEmpty())
            .doOnNext(flag -> goToExtent()));
  }

  private boolean getNewVisibilityState(DynamicLayer dynamicLayer) {

    return !mDynamicLayerVisibilityRepository.isVisible(dynamicLayer.getId());
  }

  private void setNewVisibility(Boolean newVisibilityState) {
    mDynamicLayerVisibilityRepository.addChange(
        new DynamicLayerVisibilityChange(newVisibilityState, mDynamicLayer.getId()));
  }

  private void goToExtent() {
    Geometry blockingEnvelope = mDynamicLayerEnvelopeExtractor.extract(mDynamicLayer);
    mGoToLocationMapInteractorFactory.create(blockingEnvelope).execute();
  }

  private class DynamicLayerEnvelopeExtractor {

    private ExtractPointsVisitor mPointsExtractorVisitor = new ExtractPointsVisitor();

    public Geometry extract(DynamicLayer dl) {
      List<PointGeometry> allPoints =
          getAllPoints(Lists.transform(dl.getEntities(), GeoEntity::getGeometry));
      return buildEnvelope(allPoints);
    }

    private List<PointGeometry> getAllPoints(List<Geometry> geometries) {
      List<PointGeometry> res = new ArrayList<>();
      for (Geometry g : geometries) {
        g.accept(mPointsExtractorVisitor);
        res.addAll(mPointsExtractorVisitor.getPoints());
      }
      return res;
    }

    private Geometry buildEnvelope(List<PointGeometry> allPoints) {
      List<Double> lats = Lists.transform(allPoints, PointGeometry::getLatitude);
      List<Double> longs = Lists.transform(allPoints, PointGeometry::getLongitude);

      double minX = Collections.min(lats);
      double maxX = Collections.max(lats);

      double minY = Collections.min(longs);
      double maxY = Collections.max(longs);

      return createEnvelope(minX, maxX, minY, maxY);
    }

    private Geometry createEnvelope(double minX, double maxX, double minY, double maxY) {
      PointGeometry bottomLeft = new PointGeometry(minX, minY);
      PointGeometry topLeft = new PointGeometry(minX, maxY);
      PointGeometry topRight = new PointGeometry(maxX, maxY);
      PointGeometry bottomRight = new PointGeometry(maxX, minY);
      return new Polygon(Lists.newArrayList(bottomLeft, topLeft, topRight, bottomRight));
    }

    private class ExtractPointsVisitor implements GeometryVisitor {
      List<PointGeometry> mPoints = new ArrayList<>();

      @Override
      public void visit(PointGeometry point) {
        mPoints.add(point);
      }

      @Override
      public void visit(Polygon polygon) {
        mPoints = polygon.getPoints();
      }

      @Override
      public void visit(Polyline polyline) {
        mPoints = polyline.getPoints();
      }

      public List<PointGeometry> getPoints() {
        return mPoints;
      }
    }
  }
}

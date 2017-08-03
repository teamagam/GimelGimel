package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
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
  private final DynamicLayersRepository mDynamicLayersRepository;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final DynamicLayerEnvelopeExtractor mDynamicLayerEnvelopeExtractor;
  private final String mDynamicLayerId;

  protected OnDynamicLayerListingClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerVisibilityRepository dynamicLayerVisibilityRepository,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      @Provided
          com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      String dynamicLayerId) {
    super(threadExecutor);
    mDynamicLayerVisibilityRepository = dynamicLayerVisibilityRepository;
    mDynamicLayersRepository = dynamicLayersRepository;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mDynamicLayerEnvelopeExtractor = new DynamicLayerEnvelopeExtractor();
    mDynamicLayerId = dynamicLayerId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayerId),
        dynamicLayerIdObservable -> dynamicLayerIdObservable.map(mDynamicLayersRepository::getById)
            .map(this::toPresentation)
            .doOnNext(this::setNewVisibility)
            .filter(DynamicLayerPresentation::isShown)
            .filter(this::hasEntities)
            .doOnNext(this::goToExtent));
  }

  private DynamicLayerPresentation toPresentation(DynamicLayer dynamicLayer) {
    boolean toggledVisibility = !mDynamicLayerVisibilityRepository.isVisible(dynamicLayer.getId());
    return new DynamicLayerPresentation(dynamicLayer, toggledVisibility);
  }

  private void setNewVisibility(DynamicLayerPresentation dlp) {
    mDynamicLayerVisibilityRepository.addChange(
        new DynamicLayerVisibilityChange(dlp.isShown(), dlp.getId()));
  }

  private boolean hasEntities(DynamicLayerPresentation dlp) {
    return !dlp.getEntities().isEmpty();
  }

  private void goToExtent(DynamicLayer dl) {
    Geometry blockingEnvelope = mDynamicLayerEnvelopeExtractor.extract(dl);
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

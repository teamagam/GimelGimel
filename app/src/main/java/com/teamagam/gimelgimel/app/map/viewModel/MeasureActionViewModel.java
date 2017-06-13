package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@AutoFactory
public class MeasureActionViewModel extends BaseMapViewModel {

  private final List<PointGeometry> mMeasurePoints;
  private final Collection<GeoEntity> mDrawnEntities;
  private final SpatialEngine mSpatialEngine;
  private final GGMapView mGGMapView;
  private final DecimalFormat mDecimalFormatter;
  private int mIdCount;
  private double mDistanceMeters;

  protected MeasureActionViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided SpatialEngine spatialEngine,
      GGMapView ggMapView) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);
    mSpatialEngine = spatialEngine;
    mGGMapView = ggMapView;
    mMeasurePoints = new LinkedList<>();
    mDrawnEntities = new LinkedList<>();
    mDecimalFormatter = new DecimalFormat("#.##");
    mIdCount = 0;
    mDistanceMeters = 0.0;
  }

  public void onPlusClicked() {
    mMeasurePoints.add(mGGMapView.getMapCenter());
    redrawMeasurements();
    recalculateDistance();
  }

  public double getDistanceMeters() {
    return mDistanceMeters;
  }

  public void setDistanceMeters(double distanceMeters) {
    mDistanceMeters = distanceMeters;
    notifyChange();
  }

  private void redrawMeasurements() {
    removeOldMeasurements();
    drawMeasurements();
  }

  private void removeOldMeasurements() {
    for (GeoEntity ge : mDrawnEntities) {
      mGGMapView.updateMapEntity(GeoEntityNotification.createRemove(ge));
    }
    mDrawnEntities.clear();
  }

  private void drawMeasurements() {
    if (mMeasurePoints.size() == 1) {
      drawPoint(mMeasurePoints.get(0));
    } else {
      drawPolylines(mMeasurePoints);
    }
  }

  private void drawPoint(PointGeometry pointGeometry) {
    PointEntity pointEntity = new PointEntity(generateId(), "", pointGeometry,
        new PointSymbol(false, PointSymbol.POINT_TYPE_CIRCLE));
    drawEntity(pointEntity);
  }

  private String generateId() {
    return "temp_id_" + mIdCount++;
  }

  private void drawEntity(GeoEntity pointEntity) {
    mGGMapView.updateMapEntity(GeoEntityNotification.createAdd(pointEntity));
    mDrawnEntities.add(pointEntity);
  }

  private void drawPolylines(List<PointGeometry> mMeasurePoints) {
    for (int i = 0; i < mMeasurePoints.size() - 1; i++) {
      drawPolyline(mMeasurePoints.get(i), mMeasurePoints.get(i + 1));
    }
  }

  private void drawPolyline(PointGeometry a, PointGeometry b) {
    PolylineEntity pointEntity =
        new PolylineEntity(generateId(), "", new Polyline(Arrays.asList(a, b)),
            new PolylineSymbol(false, getDistanceString(a, b)));
    drawEntity(pointEntity);
  }

  private String getDistanceString(PointGeometry a, PointGeometry b) {
    double distance = mSpatialEngine.distanceInMeters(a, b);
    return mDecimalFormatter.format(distance);
  }

  private void recalculateDistance() {
    setDistanceMeters(calculateDistance());
  }

  private double calculateDistance() {
    if (mMeasurePoints.size() == 1) {
      return 0;
    }

    return calculatePolylineDistance();
  }

  private double calculatePolylineDistance() {
    double totalDistance = 0;
    for (int i = 0; i < mMeasurePoints.size() - 1; i++) {
      totalDistance +=
          mSpatialEngine.distanceInMeters(mMeasurePoints.get(i), mMeasurePoints.get(i + 1));
    }
    return totalDistance;
  }
}
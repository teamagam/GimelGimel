package com.teamagam.gimelgimel.app.map.actions.measure;

import android.content.Context;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.BaseMapViewModel;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@AutoFactory
public class MeasureActionViewModel extends BaseMapViewModel {

  private final List<PointGeometry> mMeasurePoints;
  private final SpatialEngine mSpatialEngine;
  private final GGMapView mGGMapView;
  private final MapDrawer mMapDrawer;
  private final MapEntityFactory mMapEntityFactory;
  private final DecimalFormat mDecimalFormatter;
  private final String mColor;
  private double mDistanceMeters;

  protected MeasureActionViewModel(@Provided Context context,
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
    mMapDrawer = new MapDrawer(mGGMapView);
    mMapEntityFactory = new MapEntityFactory(new PolylineWithDistanceTextSymbolizer());
    mMeasurePoints = new LinkedList<>();
    mDecimalFormatter = new DecimalFormat("#.##");
    mDistanceMeters = 0.0;
    mColor = colorToString(context.getColor(R.color.colorAccent));
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

  private String colorToString(int color) {
    return "#" + Integer.toHexString(color).toUpperCase();
  }

  private void redrawMeasurements() {
    removeOldMeasurements();
    drawMeasurements();
  }

  private void removeOldMeasurements() {
    mMapDrawer.clear();
  }

  private void drawMeasurements() {
    if (mMeasurePoints.size() == 1) {
      drawPoint(mMeasurePoints.get(0));
    } else {
      drawPolylines(mMeasurePoints);
    }
  }

  private void drawPoint(PointGeometry pointGeometry) {
    mMapDrawer.draw(mMapEntityFactory.createPoint(pointGeometry));
  }

  private void drawPolylines(List<PointGeometry> mMeasurePoints) {
    for (int i = 0; i < mMeasurePoints.size() - 1; i++) {
      drawPolyline(mMeasurePoints.get(i), mMeasurePoints.get(i + 1));
    }
  }

  private void drawPolyline(PointGeometry a, PointGeometry b) {
    mMapDrawer.draw(mMapEntityFactory.createPolyline(Arrays.asList(a, b)));
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

  private class PolylineWithDistanceTextSymbolizer extends MapEntityFactory.SimpleSymbolizer {
    @Override
    public PolylineSymbol create(Polyline polyline) {
      List<PointGeometry> points = polyline.getPoints();
      if (points.size() == 2) {
        String distanceString = getDistanceString(points.get(0), points.get(1));
        return new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(mColor)
            .setText(distanceString)
            .build();
      }
      return super.create(polyline);
    }

    @Override
    public PointSymbol create(PointGeometry point) {
      return new PointSymbol.PointSymbolBuilder().setTintColor(mColor).build();
    }

    private String getDistanceString(PointGeometry a, PointGeometry b) {
      double distance = mSpatialEngine.distanceInMeters(a, b);
      return mDecimalFormatter.format(distance);
    }
  }
}
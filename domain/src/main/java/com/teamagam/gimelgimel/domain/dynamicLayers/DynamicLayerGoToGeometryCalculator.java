package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeometryVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicLayerGoToGeometryCalculator {

  private final DynamicLayerEnvelopeExtractor mDynamicLayerEnvelopeExtractor;

  public DynamicLayerGoToGeometryCalculator() {
    mDynamicLayerEnvelopeExtractor = new DynamicLayerEnvelopeExtractor();
  }

  public Geometry getGoToTarget(DynamicLayer dl) {
    if (hasOnePoint(dl)) {
      return getFirstGeometry(dl);
    } else {
      return mDynamicLayerEnvelopeExtractor.extract(dl);
    }
  }

  private boolean hasOnePoint(DynamicLayer dl) {
    return dl.getEntities().size() == 1 && getFirstGeometry(dl) instanceof PointGeometry;
  }

  private Geometry getFirstGeometry(DynamicLayer dl) {
    return dl.getEntities().get(0).getGeoEntity().getGeometry();
  }

  private class DynamicLayerEnvelopeExtractor {

    private ExtractPointsVisitor mPointsExtractorVisitor = new ExtractPointsVisitor();

    public Geometry extract(DynamicLayer dl) {
      List<PointGeometry> allPoints =
          getAllPoints(Lists.transform(dl.getEntities(), de -> de.getGeoEntity().getGeometry()));
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
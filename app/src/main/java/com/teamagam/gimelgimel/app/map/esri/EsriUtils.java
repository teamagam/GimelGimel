package com.teamagam.gimelgimel.app.map.esri;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeometryVisitor;
import java.util.ArrayList;
import java.util.List;

public class EsriUtils {

  public static final SpatialReference WGS_84_GEO = SpatialReferences.getWgs84();
  private static final int ED50_UTM_36_N_WKID = 23036;
  public static final SpatialReference ED50_UTM_36_N = SpatialReference.create(ED50_UTM_36_N_WKID);

  public static com.esri.arcgisruntime.geometry.Geometry transformAndProject(Geometry geometry,
      SpatialReference srcSR,
      SpatialReference dstSR) {
    return GeometryEngine.project(transform(geometry, srcSR), dstSR);
  }

  private static com.esri.arcgisruntime.geometry.Geometry transform(Geometry geometry,
      SpatialReference spatialReference) {
    return DomainToEsriGeometryTransformer.transform(geometry, spatialReference);
  }

  //public static Geometry transformAndProject(com.esri.arcgisruntime.geometry.Geometry geometry,
  //    SpatialReference srcSR,
  //    SpatialReference dstSR) {
  //  return transform(GeometryEngine.project(geometry, srcSR, dstSR));
  //}
  //
  //public static Geometry transform(com.esri.arcgisruntime.geometry.Geometry geometry) {
  //  Geometry g;
  //  if (geometry instanceof Point) {
  //    g = transformPoint((Point) geometry);
  //  } else if (geometry instanceof com.esri.arcgisruntime.geometry.Polygon) {
  //    g = transformPolygon((com.esri.arcgisruntime.geometry.Polygon) geometry);
  //  } else if (geometry instanceof com.esri.arcgisruntime.geometry.Polyline) {
  //    g = transformPolyline((com.esri.arcgisruntime.geometry.Polyline) geometry);
  //  } else if (geometry instanceof Envelope) {
  //    g = transformEnvelope((Envelope) geometry);
  //  } else {
  //    throw new RuntimeException("Unsupported geometry transformation request");
  //  }
  //  return g;
  //}
  //
  //private static Polygon transformEnvelope(Envelope envelope) {
  //  PointGeometry lowerLeft = transformPoint(envelope.getLowerLeft());
  //  PointGeometry upperLeft = transformPoint(envelope.getUpperLeft());
  //  PointGeometry upperRight = transformPoint(envelope.getUpperRight());
  //  PointGeometry lowerRight = transformPoint(envelope.getLowerRight());
  //  List<PointGeometry> points = Arrays.asList(lowerLeft, upperLeft, upperRight, lowerRight);
  //  return new Polygon(points);
  //}
  //
  //private static PointGeometry transformPoint(Point point) {
  //  return new PointGeometry(point.getY(), point.getX());
  //}
  //
  //private static Polygon transformPolygon(com.esri.arcgisruntime.geometry.Polygon polygon) {
  //  return new Polygon(transformMultiPath(polygon));
  //}
  //
  //private static List<PointGeometry> transformMultiPath(MultiPath multipath) {
  //  int pointCount = multipath.getPointCount();
  //  List<PointGeometry> points = new ArrayList<>(pointCount);
  //  for (int i = 0; i < pointCount; i++) {
  //    points.add(transformPoint(multipath.getPoint(i)));
  //  }
  //  return points;
  //}
  //
  //private static Polyline transformPolyline(com.esri.arcgisruntime.geometry.Polyline polyline) {
  //  return new Polyline(transformMultiPath(polyline));
  //}
  //
  private static class DomainToEsriGeometryTransformer implements GeometryVisitor {

    private final SpatialReference mSpatialReference;
    private com.esri.arcgisruntime.geometry.Geometry mResult;

    public DomainToEsriGeometryTransformer(SpatialReference spatialReference) {
      mSpatialReference = spatialReference;
    }

    static com.esri.arcgisruntime.geometry.Geometry transform(Geometry geometry,
        SpatialReference spatialReference) {
      DomainToEsriGeometryTransformer visitor =
          new DomainToEsriGeometryTransformer(spatialReference);
      geometry.accept(visitor);
      return visitor.mResult;
    }

    private Point transformPoint(PointGeometry point) {
      if (point.hasAltitude()) {
        return new Point(point.getLongitude(), point.getLatitude(), point.getAltitude(),
            mSpatialReference);
      } else {
        return new Point(point.getLongitude(), point.getLatitude(), mSpatialReference);
      }
    }

    private com.esri.arcgisruntime.geometry.Polygon transformPolygon(Polygon polygon) {
      List<Point> points = transformPoints(polygon.getPoints());
      return new com.esri.arcgisruntime.geometry.Polygon(new PointCollection(points));
    }

    private com.esri.arcgisruntime.geometry.Polyline transformPolyline(Polyline polyline) {
      List<Point> points = transformPoints(polyline.getPoints());
      return new com.esri.arcgisruntime.geometry.Polyline(new PointCollection(points));
    }

    private List<Point> transformPoints(List<PointGeometry> points) {
      List<Point> list = new ArrayList<>();
      for (int i = 0; i < points.size(); i++) {
        list.add(transformPoint(points.get(i)));
      }
      return list;
    }

    @Override
    public void visit(PointGeometry point) {
      mResult = transformPoint(point);
    }

    @Override
    public void visit(Polygon polygon) {
      mResult = transformPolygon(polygon);
    }

    @Override
    public void visit(Polyline polyline) {
      mResult = transformPolyline(polyline);
    }
  }
}

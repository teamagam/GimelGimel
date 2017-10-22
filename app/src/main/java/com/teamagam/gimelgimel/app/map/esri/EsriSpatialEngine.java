package com.teamagam.gimelgimel.app.map.esri;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.LinearUnit;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import javax.inject.Inject;

public class EsriSpatialEngine implements SpatialEngine {

  @Inject
  public EsriSpatialEngine() {

  }

  @Override
  public double distanceInMeters(PointGeometry point1, PointGeometry point2) {
    return 0;
  }

  @Override
  public PointGeometry projectToUTM(PointGeometry pointGeometry) {
    return null;
  }

  @Override
  public PointGeometry projectFromUTM(PointGeometry point) {
    return null;
  }

  //@Override
  //public double distanceInMeters(PointGeometry point1, PointGeometry point2) {
  //  return GeometryEngine.geodesicDistance((Point) EsriUtils.transform(point1),
  //      (Point) EsriUtils.transform(point2), EsriUtils.WGS_84_GEO,
  //      (LinearUnit) LinearUnit.create(LinearUnit.Code.METER));
  //}
  //
  //@Override
  //public PointGeometry projectToUTM(PointGeometry pointGeometry) {
  //  return project(pointGeometry, EsriUtils.WGS_84_GEO, EsriUtils.ED50_UTM_36_N);
  //}
  //
  //@Override
  //public PointGeometry projectFromUTM(PointGeometry pointGeometry) {
  //  return project(pointGeometry, EsriUtils.ED50_UTM_36_N, EsriUtils.WGS_84_GEO);
  //}
  //
  //private PointGeometry project(PointGeometry pointGeometry,
  //    SpatialReference from,
  //    SpatialReference to) {
  //  Point point = (Point) EsriUtils.transformAndProject(pointGeometry, from, to);
  //  return new PointGeometry(point.getY(), point.getX());
  //}
}

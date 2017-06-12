package com.teamagam.gimelgimel.data.location.adpater;

import com.teamagam.geogson.core.model.Point;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.response.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import javax.inject.Inject;

public class LocationSampleDataAdapter {

  private final GeometryDataMapper mGeometryDataMapper;

  @Inject
  public LocationSampleDataAdapter(GeometryDataMapper geometryDataMapper) {
    mGeometryDataMapper = geometryDataMapper;
  }

  public LocationSampleData transformToData(LocationSample locationSample) {
    Point pointData = mGeometryDataMapper.transformToData(locationSample.getLocation());
    return new LocationSampleData(locationSample, pointData);
  }

  public LocationSample transform(LocationSampleData content) {
    PointGeometry point = mGeometryDataMapper.transform(content.getLocation());

    return new LocationSample(point, content.getTime(), content.getProvider(), content.hasSpeed(),
        content.getSpeed(), content.hasBearing(), content.getBearing(), content.hasAccuracy(),
        content.getAccuracy());
  }
}

package com.teamagam.gimelgimel.data.location.adpater;

import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

import javax.inject.Inject;

/**
 * Created on 10/30/2016.
 * TODO: complete text
 */

public class LocationSampleDataAdapter {

    private final GeometryDataMapper mGeometryDataMapper;

    @Inject
    public LocationSampleDataAdapter(GeometryDataMapper geometryDataMapper){
        mGeometryDataMapper = geometryDataMapper;
    }

    public LocationSampleData transformToData(LocationSampleEntity locationSampleEntity) {
        PointGeometryData pointGeometryData =
                mGeometryDataMapper.transformToData(locationSampleEntity.getLocation());
        return new LocationSampleData(locationSampleEntity, pointGeometryData);
    }

    public LocationSampleEntity transform(LocationSampleData content) {
        LocationSampleEntity convertedLocationSampleEntity =
                new LocationSampleEntity(mGeometryDataMapper.transform(content.getLocation()),
                        content.getTime());

        if (content.hasAccuracy()) {
            convertedLocationSampleEntity.setAccuracy(content.getAccuracy());
        }
        if (content.hasBearing()) {
            convertedLocationSampleEntity.setBearing(content.getBearing());
        }
        if (content.hasSpeed()) {
            convertedLocationSampleEntity.setSpeed(content.getSpeed());
        }

        return convertedLocationSampleEntity;
    }
}

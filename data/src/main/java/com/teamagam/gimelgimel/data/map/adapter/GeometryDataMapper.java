package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.gimelgimel.data.map.entity.GeometryData;
import com.teamagam.gimelgimel.data.map.entity.MultiPointGeometryData;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created on 8/10/2016.
 * TODO: complete text
 */
@Singleton
public class GeometryDataMapper {

    @Inject
    public GeometryDataMapper(){

    }

    public Geometry transform(GeometryData geoData) {
        Geometry geo = null;
        if (geoData != null) {
            geo = geoData.transformToEntity();
        }

        return geo;
    }

    public List<Geometry> transform(Collection<GeometryData> userEntityCollection) {
        List<Geometry> userList = new ArrayList<>(20);
        Geometry user;
        for (GeometryData userEntity : userEntityCollection) {
            user = transform(userEntity);
            if (user != null) {
                userList.add(user);
            }
        }

        return userList;
    }

    public PointGeometry transform(PointGeometryData point){
        return (PointGeometry) transform((GeometryData) point);
    }

    public GeometryData transformToData(Geometry location) {
        return new GeometryToDataTransformer().transformToData(location);
    }

    public PointGeometryData transformToData(PointGeometry point){
        return (PointGeometryData) transformToData((Geometry) point);
    }

    private class GeometryToDataTransformer implements IGeometryVisitor{

        GeometryData mGeometryData;

        private GeometryData transformToData(Geometry geo){
            geo.accept(this);
            return mGeometryData;
        }

        @Override
        public void visit(PointGeometry geometry) {
            double latitude = geometry.getLatitude();
            double longitude = geometry.getLongitude();
            if(geometry.hasAltitude()) {
                    mGeometryData =  new PointGeometryData(latitude, longitude, geometry.getAltitude());
                } else {
                    mGeometryData = new PointGeometryData(latitude, longitude);
                }
            }

        @Override
        public void visit(MultiPointGeometry multiPointGeometry) {
            Collection<PointGeometryData> geoCollection = new LinkedList<>();
            for(PointGeometry point : multiPointGeometry.pointsCollection  ){
                geoCollection.add((PointGeometryData) transformToData(point));
            }
            mGeometryData = new MultiPointGeometryData(geoCollection);
        }
    }

}

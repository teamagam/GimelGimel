package com.teamagam.gimelgimel.data.entities.geometries.mapper;

import com.teamagam.gimelgimel.data.entities.geometries.GeometryData;
import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created on 8/10/2016.
 * TODO: complete text
 */
public class GeometryDataMapper {

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

}

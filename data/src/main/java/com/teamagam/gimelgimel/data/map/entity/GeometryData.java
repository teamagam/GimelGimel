package com.teamagam.gimelgimel.data.map.entity;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;

/**
 * Created by Bar on 03-Mar-16.
 *
 * Immutable
 */
public interface GeometryData {
    Geometry transformToEntity();
    //in the future: getArea, translate, transform, etc...
}

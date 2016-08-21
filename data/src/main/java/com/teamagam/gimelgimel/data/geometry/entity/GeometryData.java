package com.teamagam.gimelgimel.data.geometry.entity;

import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;

/**
 * Created by Bar on 03-Mar-16.
 *
 * Immutable
 */
public interface GeometryData {
    Geometry transformToEntity();
    //in the future: getArea, translate, transform, etc...
}

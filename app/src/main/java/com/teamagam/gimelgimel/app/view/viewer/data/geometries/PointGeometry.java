package com.teamagam.gimelgimel.app.view.viewer.data.geometries;

/**
 * Created by Bar on 29-Feb-16.
 *
 * Simple data-object for latitude/longitude location
 */
public class PointGeometry implements Geometry{

    public double latitude;
    public double longitude;

    public PointGeometry(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

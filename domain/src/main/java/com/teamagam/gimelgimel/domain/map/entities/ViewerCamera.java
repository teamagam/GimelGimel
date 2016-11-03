package com.teamagam.gimelgimel.domain.map.entities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

/**
 * Created on 11/3/2016.
 * TODO: complete text
 */
public class ViewerCamera {

    public final PointGeometry cameraPosition;

    public final float heading;
    public final float pitch;
    public final float roll;

    public ViewerCamera(PointGeometry cameraPosition,
            float heading, float pitch, float roll) {
        this.cameraPosition = cameraPosition;
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }

//    public float getHeading() {
//        return heading;
//    }
//
//    public float getPitch() {
//        return pitch;
//    }
//
//    public float getRoll() {
//        return roll;
//    }
//
//    public PointGeometry getCameraPosition() {
//        return cameraPosition;
//    }

}

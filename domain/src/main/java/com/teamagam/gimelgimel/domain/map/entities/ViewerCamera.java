package com.teamagam.gimelgimel.domain.map.entities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

/**
 * Created on 11/3/2016.
 * TODO: complete text
 */
public class ViewerCamera {

    public final PointGeometry cameraPosition;

    /**
     * A rotation expressed as a heading, pitch, and roll.
     * Heading is the rotation about the negative z axis.
     * Pitch is the rotation about the negative y axis.
     * Roll is the rotation about the positive x axis.
     */
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
}

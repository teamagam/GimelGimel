package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.Location;

import java.util.Collection;

/**
 * Created by Bar on 29-Feb-16.
 */
public class Polygon extends MultipleLocationsEntity {

    public Polygon(String id,
                   Collection<Location> locations) {
        super(id, locations);
    }

    public Polygon(String id){
        super(id);
    }


}

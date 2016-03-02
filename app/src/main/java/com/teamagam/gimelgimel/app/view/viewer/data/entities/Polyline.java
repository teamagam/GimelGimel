package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.Location;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bar on 29-Feb-16.
 */
public class Polyline extends MultipleLocationsEntity {

    public Polyline(String id,
                    Collection<Location> locations) {
        super(id, locations);
    }

    public Polyline(String id) {
        super(id);
    }
}

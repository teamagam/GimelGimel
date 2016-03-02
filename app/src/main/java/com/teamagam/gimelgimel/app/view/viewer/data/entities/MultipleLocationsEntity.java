package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.Location;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Bar on 29-Feb-16.
 */
public abstract class MultipleLocationsEntity extends AbsEntity {

    private Collection<Location> mLocations;

    public MultipleLocationsEntity(String id, Collection<Location> locations) {
        super(id);
        this.mLocations = locations;
    }

    public MultipleLocationsEntity(String id) {
        this(id, new ArrayList<Location>());
    }


    public Collection<Location> getLocations() {
        return mLocations;
    }

    public void updateLocations(Collection<Location> locations){
        mLocations = locations;
        mEntityChangedListener.OnEntityChanged(this);
    }
}

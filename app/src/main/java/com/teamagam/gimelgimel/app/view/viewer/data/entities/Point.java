package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.Location;

/**
 * Created by Bar on 29-Feb-16.
 */
public class Point extends AbsEntity {

    private Location mLocation;

    public static Point CreatePoint(double lat, double lng, String id) {
        Location loc = new Location(lat, lng);
        return new Point(loc, id);
    }

    //TODO: enable instantiation via some builder-pattern that manages ids
    public Point(Location location, String id) {
        super(id);
        this.mLocation = location;
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public void updateLocation(Location location){
        mLocation = location;
        mEntityChangedListener.OnEntityChanged(this);
    }
}

package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.Location;

/**
 * Created by Bar on 29-Feb-16.
 */
public interface Entity {

    String getId();

    void setOnEntityChangedListener(EntityChangedListener ecl);

    void removeOnEntityChangedListener();


    interface EntityChangedListener{
        void OnEntityChanged(Entity changedEntity);
    }

    //Implementations should provide implementation for suitable method in GeographicLocatorVisitor
}

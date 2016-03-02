package com.teamagam.gimelgimel.app.view.viewer.data.entities;

/**
 * Created by Bar on 29-Feb-16.
 *
 * Defines functionality needed for a vector entity
 *
 */
public interface Entity {
    //TODO: think of a good way to represent spatial data (maybe use a visitor pattern to extract?)

    String getId();

    void setOnEntityChangedListener(EntityChangedListener ecl);

    void removeOnEntityChangedListener();

    interface EntityChangedListener{
        void OnEntityChanged(Entity changedEntity);
    }
}

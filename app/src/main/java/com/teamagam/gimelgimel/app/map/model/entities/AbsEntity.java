package com.teamagam.gimelgimel.app.map.model.entities;


/**
 * An abstract class implementing id handling and
 * the EntityChangedListener registration and its removal
 */
public abstract class AbsEntity implements Entity {

    protected final String mId;

    protected AbsEntity(String id) {
        mId = id;
    }

    @Override
    public String getId() {
        return mId;
    }

}

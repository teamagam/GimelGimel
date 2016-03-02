package com.teamagam.gimelgimel.app.view.viewer.data.entities;

/**
 * Created by Bar on 02-Mar-16.
 *
 * An abstract class implementing id handling and
 * the EntityChangedListener registration and its removal
 */
public abstract class AbsEntity implements Entity{

    protected String mId;

    protected EntityChangedListener mEntityChangedListener;

    public AbsEntity(String id){
        mId = id;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setOnEntityChangedListener(EntityChangedListener ecl) {
        mEntityChangedListener = ecl;
    }

    @Override
    public void removeOnEntityChangedListener() {
        mEntityChangedListener = null;
    }
}

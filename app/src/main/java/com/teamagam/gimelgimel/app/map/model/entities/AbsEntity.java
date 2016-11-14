package com.teamagam.gimelgimel.app.map.model.entities;


/**
 * An abstract class implementing id handling and
 * the EntityChangedListener registration and its removal
 */
public abstract class AbsEntity implements Entity {

    protected final String mId;

    protected final String mText;

    protected AbsEntity(String id, String text) {
        mId = id;
        mText = text;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getText() {
        return mText;
    }

}

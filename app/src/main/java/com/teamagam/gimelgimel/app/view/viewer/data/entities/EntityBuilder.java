package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.Geometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

/**
 * Created by Yoni on 3/15/2016.
 */
public abstract class EntityBuilder<B extends EntityBuilder<B,E>,  E extends AbsEntity>{

    public static final String LOG_TAG = EntityBuilder.class.getSimpleName();

    protected static long numId = 0;
    protected String mId;

    protected Geometry mGeometry;
    protected Symbol mSymbol;

    public EntityBuilder(String id) {
        this();
        //set different id if required.
        mId = id;
    }

    public EntityBuilder() {
        mId = String.format("%s_%d", getIdPrefix(), numId++);
    }

    public B setId(String id) {
        mId = id;
        return getThis();
    }

    public B setGeometry(Geometry geometry) {
        mGeometry = geometry;
        return getThis();
    }

    public B setSymbol(Symbol symbol) {
        mSymbol = symbol;
        return getThis();
    }

    protected abstract B getThis();

    protected abstract String getIdPrefix();

    public abstract E build();

}

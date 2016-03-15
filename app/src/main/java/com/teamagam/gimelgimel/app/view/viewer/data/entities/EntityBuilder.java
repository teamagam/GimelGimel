package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.Geometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

import java.lang.ref.WeakReference;

/**
 * Created by Yoni on 3/15/2016.
 */
public abstract class EntityBuilder<E extends AbsEntity>{

    /**
     * Weak reference to listener.
     * <p/>
     * A weak reference is used to allow registering objects to be
     * freed by the GC, despite their registration as a listener
     */
    protected WeakReference<Entity.EntityChangedListener> mWRBuilderEntityChangedListener ;

    public static final String LOG_TAG = EntityBuilder.class.getSimpleName();
    protected String mId;
    protected Geometry mGeometry;
    protected Symbol mSymbol;

    public EntityBuilder(String id) {
        this();
        //todo: manage ids!
        mId = id;
    }

    public EntityBuilder() {
        mWRBuilderEntityChangedListener = null;
    }

    public EntityBuilder setId(String id) {
        mId = id;
        return this;
    }

    public EntityBuilder setGeometry(Geometry geometry) {
        mGeometry = geometry;
        return this;
    }

    public EntityBuilder setSymbol(Symbol symbol) {
        mSymbol = symbol;
        return this;
    }

    public abstract E create();

    interface EntityBuilderInterface<E extends AbsEntity> {
        E create();
    }
}

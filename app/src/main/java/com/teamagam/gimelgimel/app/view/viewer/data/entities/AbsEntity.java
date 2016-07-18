package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;

import java.lang.ref.WeakReference;

/**
 * An abstract class implementing id handling and
 * the EntityChangedListener registration and its removal
 */
public abstract class AbsEntity implements Entity {

    private static final Logger sLogger = LoggerFactory.create(AbsEntity.class);
    protected final String mId;

    /**
     * Weak reference to listener.
     * <p/>
     * A weak reference is used to allow registering objects to be
     * freed by the GC, despite their registration as a listener
     */
    protected WeakReference<EntityChangedListener> mWREntityChangedListener;
    protected WeakReference<OnClickListener> mWROnClickListener;

    protected AbsEntity(String id) {
        mId = id;
        mWREntityChangedListener = null;
        mWROnClickListener = null;
    }

    @Override
    public String getId() {
        return mId;
    }

    /**
     * Keeps reference to the given listener with a {@link WeakReference} to be
     * used when an entity changes, to notify listener.
     * <br/>
     * <b>Use caution</b> - do not add an unreferenced listener, as it would be
     * collected by the GC (anonymous listeners)
     * <br/>
     * <b>Overrides</b> former (if any) listener registration
     *
     * @param entityChangedListener - new listener to be fired on change events
     */
    @Override
    public void setOnEntityChangedListener(EntityChangedListener entityChangedListener) {
        if (mWREntityChangedListener != null && mWREntityChangedListener.get() != null) {
            sLogger.d("OnEntityChanged listener override for entity-id " + mId);
        }

        mWREntityChangedListener = new WeakReference<>(entityChangedListener);
    }

    @Override
    public void removeOnEntityChangedListener() {
        if (mWREntityChangedListener == null) {
            //No listener attached
            sLogger.d("removeOnEntityChangedListener called with no listener attached");
            return;
        }

        mWREntityChangedListener.clear();
    }

    /**
     * {@link WeakReference} to {@link OnClickListener}.
     *
     * see: setOnEntityChangedListener
     * @param onClickListener
     */
    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        if (mWROnClickListener != null && mWROnClickListener.get() != null) {
            sLogger.d("OnClickListener listener override for entity-id " + mId);
        }

        mWROnClickListener = new WeakReference<>(onClickListener);
    }

    @Override
    public void removeOnClickListener() {
        if (mWROnClickListener == null) {
            //No listener attached
            sLogger.d("removeOnClickListener called with no listener attached");
            return;
        }

        mWROnClickListener.clear();
    }

    protected void fireEntityChanged() {
        if (mWREntityChangedListener == null) {
            //No listener attached
            sLogger.d("fireEntityChanged called with no listener attached");
            return;
        }

        EntityChangedListener listener = mWREntityChangedListener.get();
        if (listener == null) {
            sLogger.d("fireEntityChanged called while WeakReference's referent is null");
            return;
        }

        listener.onEntityChanged(this);
    }

    @Override
    public void clicked() {
        if (mWROnClickListener == null) {
            //No listener attached
            sLogger.d("clicked called with no listener attached");
            return;
        }

        OnClickListener listener = mWROnClickListener.get();
        if (listener == null) {
            sLogger.d("clicked called while WeakReference's referent is null");
            return;
        }

        listener.onEntityClick(this);
    }
}

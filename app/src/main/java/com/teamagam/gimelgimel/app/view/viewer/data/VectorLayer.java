package com.teamagam.gimelgimel.app.view.viewer.data;


import android.util.Log;

import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;

/**
 * A class that manages a collection of {@link Entity}
 * Using classes can implement {@link LayerChangedListener} and
 * register for contained entities changes
 */
public class VectorLayer extends GGLayer implements Entity.EntityChangedListener {

    public static final String LOG_TAG = VectorLayer.class.getSimpleName();
    private HashMap<String, Entity> mIdToEntityHashMap;
    private WeakReference<LayerChangedListener> mWRLayerChangedListener;

    //TODO: enable instantiation via some builder-pattern that manages ids
    public VectorLayer(String id) {
        super(id);
        mIdToEntityHashMap = new HashMap<>();
        mWRLayerChangedListener = null;
    }

    public void addEntity(Entity entity) {
        if (mIdToEntityHashMap.containsKey(entity.getId())) {
            throw new IllegalArgumentException("An entity with this id already exists in layer");
        }

        mIdToEntityHashMap.put(entity.getId(), entity);
        entity.setOnEntityChangedListener(this);

        LayerChangedEventArgs layerChangedEventArgs = new LayerChangedEventArgs(mId, entity,
                LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_ADD);
        fireListener(layerChangedEventArgs);
    }

    public Entity removeEntity(String entityId) {
        if (!mIdToEntityHashMap.containsKey(entityId)) {
            throw new IllegalArgumentException("An entity with this id doesn't exist in layer");
        }

        Entity entity = mIdToEntityHashMap.get(entityId);

        LayerChangedEventArgs args = new LayerChangedEventArgs(mId, entity,
                LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_REMOVE);
        fireListener(args);

        Entity res = mIdToEntityHashMap.remove(entity.getId());
        entity.removeOnEntityChangedListener();

        return res;
    }

    public Collection<Entity> getEntities() {
        return mIdToEntityHashMap.values();
    }

    public Entity getEntity(String id) {
        Entity res = mIdToEntityHashMap.get(id);
        return res;
    }


    /**
     * Keeps reference to the given listener with a {@link WeakReference} to be
     * used when a layer changes, to notify listener.
     * <p/>
     * <b>Use caution</b> - do not add an unreferenced listener, as it would be
     * collected by the GC  (anonymous listeners)
     * <b>Overrides</b> former (if any) listener registration
     *
     * @param listener - new listener to be fired on changed events
     */
    public void setOnLayerChangedListener(LayerChangedListener listener) {
        if (mWRLayerChangedListener != null && mWRLayerChangedListener.get() != null) {
            Log.d(LOG_TAG, "OnLayerChanged listener override for entity-id " + mId);
        }

        mWRLayerChangedListener = new WeakReference<>(listener);
    }

    public void removeLayerChangedListener() {
        if (mWRLayerChangedListener == null) {
            //No listener attached
            Log.d(LOG_TAG, "removeLayerChangedListener called with no listener attached");
            return;
        }

        mWRLayerChangedListener.clear();
    }

    @Override
    public void onEntityChanged(Entity changedEntity) {
        LayerChangedEventArgs args = new LayerChangedEventArgs(mId, changedEntity,
                LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_UPDATE);
        fireListener(args);
    }

    private void fireListener(LayerChangedEventArgs layerChangedEventArgs) {
        if (mWRLayerChangedListener == null) {
            //No listener attached
            Log.d(LOG_TAG, "fireListener called with no listener attached");
            return;
        }

        LayerChangedListener listener = mWRLayerChangedListener.get();
        if (listener == null) {
            Log.d(LOG_TAG,
                    "fireListener called while WeakReference's referent is null (perhaps already freed by GC)");
            return;
        }

        listener.layerChanged(layerChangedEventArgs);
    }

    /**
     * An interface needed to be implemented to register as a listener
     * for layer changes events
     */
    public interface LayerChangedListener {
        void layerChanged(LayerChangedEventArgs eventArgs);
    }
}

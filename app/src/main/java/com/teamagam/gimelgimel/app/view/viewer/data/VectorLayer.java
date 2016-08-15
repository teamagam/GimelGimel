package com.teamagam.gimelgimel.app.view.viewer.data;


import com.gimelgimel.domain.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.utils.InputValidationUtils;
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

    private static final Logger sLogger = LoggerFactory.create(VectorLayer.class);
    private HashMap<String, Entity> mIdToEntityHashMap;
    private WeakReference<LayerChangedListener> mWRLayerChangedListener;

    //TODO: enable instantiation via some builder-pattern that manages ids
    public VectorLayer(String id) {
        super(id);
        mIdToEntityHashMap = new HashMap<>();
        mWRLayerChangedListener = null;
    }


    /**
     * Adds given entity to this layer and registers for entity change events.<br/>
     * Raises appropriate layer changed event.
     *
     * @param entity entity to add.<br/>
     *               Entity's id must be unique and non-empty within layer
     */
    public void addEntity(Entity entity) {
        InputValidationUtils.notNull(entity, "entity");
        InputValidationUtils.stringNotNullOrEmpty(entity.getId(), "entity's id");

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
        InputValidationUtils.stringNotNullOrEmpty(entityId, "entityId");

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

    public void removeAllEntities() {
        Entity[] entities = getEntities().toArray(new Entity[0]);
        for (Entity entity : entities) {
            removeEntity(entity.getId());
        }
    }

    /**
     * @return collection of entities in vector-layer
     */
    public Collection<Entity> getEntities() {
        return mIdToEntityHashMap.values();
    }

    /**
     * Returns entity mapped to given id
     *
     * @param id to look for
     * @return an entity with matching id inside this vector layer,
     * or null if no entity with given id exists
     */
    public Entity getEntity(String id) {
        InputValidationUtils.stringNotNullOrEmpty(id, "id");

        return mIdToEntityHashMap.get(id);
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
            sLogger.d("OnLayerChanged listener override for entity-id " + mId);
        }

        mWRLayerChangedListener = new WeakReference<>(listener);
    }

    public void removeLayerChangedListener() {
        if (mWRLayerChangedListener == null) {
            //No listener attached
            sLogger.d("removeLayerChangedListener called with no listener attached");
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
            sLogger.d("fireListener called with no listener attached");
            return;
        }

        LayerChangedListener listener = mWRLayerChangedListener.get();
        if (listener == null) {
            sLogger.d(
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

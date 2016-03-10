package com.teamagam.gimelgimel.app.view.viewer.data;


import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Bar on 29-Feb-16.
 * <p/>
 * A class that manages a collection of {@link Entity}
 * Using classes can implement {@link LayerChangedListener} and
 * register for contained entities changes
 */
public class VectorLayer extends GGLayer implements Entity.EntityChangedListener {

    private HashMap<String, Entity> mIdToEntityHashMap;
    private Collection<LayerChangedListener> mListeners;

    //TODO: enable instantiation via some builder-pattern that manages ids
    public VectorLayer(String id) {
        super(id);
        mIdToEntityHashMap = new HashMap<>();
        mListeners = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        if (mIdToEntityHashMap.containsKey(entity.getId())) {
            throw new IllegalArgumentException("An entity with this id already exists in layer");
        }

        mIdToEntityHashMap.put(entity.getId(), entity);
        entity.setOnEntityChangedListener(this);

        LayerChangedEventArgs layerChangedEventArgs = new LayerChangedEventArgs(mId, entity,
                LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_ADD);
        fireListeners(layerChangedEventArgs);
    }

    public Entity removeEntity(String entityId) {
        if (!mIdToEntityHashMap.containsKey(entityId)) {
            throw new IllegalArgumentException("An entity with this id doesn't exist in layer");
        }

        Entity entity = mIdToEntityHashMap.get(entityId);

        LayerChangedEventArgs args = new LayerChangedEventArgs(mId, entity,
                LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_REMOVE);
        fireListeners(args);

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

    public void addLayerChangedListener(LayerChangedListener listener) {
        mListeners.add(listener);
    }

    public void removeLayerChangedListener(LayerChangedListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void OnEntityChanged(Entity changedEntity) {
        LayerChangedEventArgs args = new LayerChangedEventArgs(mId, changedEntity,
                LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_UPDATE);
        fireListeners(args);
    }

    private void fireListeners(LayerChangedEventArgs layerChangedEventArgs) {
        for (LayerChangedListener listener : mListeners) {
            listener.LayerChanged(layerChangedEventArgs);
        }
    }

    public interface LayerChangedListener {
        void LayerChanged(LayerChangedEventArgs eventArgs);
    }
}

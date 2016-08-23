package com.teamagam.gimelgimel.domain.geometries.entities;

import java.util.Collection;
import java.util.HashMap;

public class VectorLayer {

    private String mLayerId;
    private HashMap<String, GeoEntity> mIdToEntityHashMap;

    public VectorLayer(String id) {
        mLayerId = id;
        mIdToEntityHashMap = new HashMap<>();
    }

    /**
     * Adds given entity to this layer and registers for entity change events.<br/>
     * Raises appropriate layer changed event.
     *
     * @param entity entity to add.<br/>
     *               Entity's id must be unique and non-empty within layer
     */
    public void addEntity(GeoEntity entity) {
        if (mIdToEntityHashMap.containsKey(entity.getId())) {
            throw new IllegalArgumentException("An entity with this id already exists in layer");
        }

        mIdToEntityHashMap.put(entity.getId(), entity);
    }

    public void addEntities(Collection<GeoEntity> entities) {
        for (GeoEntity entity : entities) {
            addEntity(entity);
        }
    }

    public GeoEntity removeEntity(String entityId) {
        if (!mIdToEntityHashMap.containsKey(entityId)) {
            throw new IllegalArgumentException("An entity with this id doesn't exist in layer");
        }

        GeoEntity entity = mIdToEntityHashMap.get(entityId);
        GeoEntity res = mIdToEntityHashMap.remove(entity.getId());

        return res;
    }

    public void removeAllEntities() {
        GeoEntity[] entities = getEntities().toArray(new GeoEntity[0]);
        for (GeoEntity entity : entities) {
            removeEntity(entity.getId());
        }
    }

    public String getLayerId() {
        return mLayerId;
    }

    /**
     * @return collection of entities in vector-layer
     */
    public Collection<GeoEntity> getEntities() {
        return mIdToEntityHashMap.values();
    }

    /**
     * Returns entity mapped to given id
     *
     * @param id to look for
     * @return an entity with matching id inside this vector layer,
     * or null if no entity with given id exists
     */
    public GeoEntity getEntity(String id) {
        return mIdToEntityHashMap.get(id);
    }
}

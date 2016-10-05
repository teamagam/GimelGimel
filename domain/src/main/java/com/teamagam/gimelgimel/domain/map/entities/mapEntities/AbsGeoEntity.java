package com.teamagam.gimelgimel.domain.map.entities.mapEntities;


public abstract class AbsGeoEntity implements GeoEntity{

    private String mId;
    private String mLayerTag;

    public AbsGeoEntity(String id, String layerTag) {
        mId = id;
        mLayerTag = layerTag;
    }


    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getLayerTag() {
        return mLayerTag;
    }
}

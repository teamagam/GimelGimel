package com.teamagam.gimelgimel.domain.map.entities.mapEntities;


abstract class AbsGeoEntity implements GeoEntity{

    private String mId;
    private String mLayerTag;
    private String mText;

    public AbsGeoEntity(String id, String text) {
        mId = id;
        mText = text;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setLayerTag(String mLayerTag) {
        this.mLayerTag = mLayerTag;
    }


    public String getText() {
        return mText;
    }

    @Override
    public String getLayerTag() {
        return mLayerTag;
    }
}

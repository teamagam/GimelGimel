package com.teamagam.gimelgimel.domain.map.entities;


public class BaseGeoEntity implements GeoEntity {

    private String mId;
    private Geometry mGeometry;
    private Symbol mSymbol;
    private String mLayerTag;

    public BaseGeoEntity(String id, Geometry geometry, Symbol symbol, String layerTag) {
        mId = id;
        mLayerTag = layerTag;
        mGeometry = geometry;
        mSymbol = symbol;
    }


    @Override
    public String getId() {
        return mId;
    }

    @Override
    public Geometry getGeometry() {
        return mGeometry;
    }

    @Override
    public Symbol getSymbol() {
        return mSymbol;
    }

    @Override
    public String getLayerTag() {
        return mLayerTag;
    }
}

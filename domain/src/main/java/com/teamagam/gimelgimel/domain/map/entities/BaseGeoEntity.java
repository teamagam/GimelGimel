package com.teamagam.gimelgimel.domain.map.entities;

public class BaseGeoEntity implements GeoEntity {

    private String mId;
    private Geometry mGeometry;
    private Symbol mSymbol;

    public BaseGeoEntity(String id, Geometry geometry, Symbol symbol) {
        mId = id;
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

//    @Override
//    public void updateGeometry(Geometry geo) {
//
//    }
//
//    @Override
//    public void updateSymbol(Symbol symbol) {
//
//    }
}

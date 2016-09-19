package com.teamagam.gimelgimel.domain.messages.entity;


import com.teamagam.gimelgimel.domain.geometries.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

public class MessageGeo extends Message {

    private GeoEntity mGeoEntity;
    private String mText;
    private String mType;

    public MessageGeo(String senderId, GeoEntity geoEntity, String text, String type) {
        super(senderId);

        mGeoEntity = geoEntity;
        mText = text;
        mType = type;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public void setGeoEntity(GeoEntity mGeoEntity) {
        this.mGeoEntity = mGeoEntity;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setType(String type) {
        mType = type;
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }

    public String getText() {
        return mText;
    }

    public String getType() {
        return mType;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        if (!mText.isEmpty()) {
            s.append("text=" + mText);
        } else {
            s.append("text=?");
        }
        s.append("entity= " + mGeoEntity);
        s.append(']');
        return s.toString();
    }
}



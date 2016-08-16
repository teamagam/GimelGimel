package com.teamagam.gimelgimel.domain.messages.entity;


import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

public class MessageGeo extends Message {

    private PointGeometry mLocation;
    private String mText;
    private String mType;

    public MessageGeo(String senderId, PointGeometry location, String text, String type) {
        super(senderId);

        mLocation = location;
        mText = text;
        mType = type;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public void setLocation(PointGeometry location) {
        mLocation = location;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setType(String type) {
        mType = type;
    }

    public PointGeometry getLocation() {
        return mLocation;
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
        s.append("type=" + mType);
        s.append("point=" + mLocation);
        if (!mText.isEmpty()) {
            s.append("text=" + mText);
        } else {
            s.append("text=?");
        }
        s.append(']');
        return s.toString();
    }
}



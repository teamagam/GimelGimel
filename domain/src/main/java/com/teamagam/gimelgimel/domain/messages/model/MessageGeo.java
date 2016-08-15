package com.teamagam.gimelgimel.domain.messages.model;


import com.teamagam.gimelgimel.domain.messages.model.visitor.IMessageVisitor;

public class MessageGeo extends MessageModel {

    private com.teamagam.gimelgimel.domain.messages.model.PointGeometry mLocation;
    private String mText;
    private String mType;

    public MessageGeo(String senderId, com.teamagam.gimelgimel.domain.messages.model.PointGeometry location, String text, String type) {
        super(senderId);

        mLocation = location;
        mText = text;
        mType = type;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public void setLocation(com.teamagam.gimelgimel.domain.messages.model.PointGeometry location) {
        mLocation = location;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setType(String type) {
        mType = type;
    }

    public com.teamagam.gimelgimel.domain.messages.model.PointGeometry getLocation() {
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



package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.GeoContent;

/**
 * A class for geo messages (using GeoContent as its content)
 */
public class MessageGeo extends Message<GeoContent>{

    public MessageGeo(String senderId, GeoContent location) {
        super(senderId, Message.GEO);
        mContent = location;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}



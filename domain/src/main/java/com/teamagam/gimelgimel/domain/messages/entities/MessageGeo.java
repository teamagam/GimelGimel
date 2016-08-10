package com.teamagam.gimelgimel.domain.messages.entities;

import com.teamagam.gimelgimel.domain.messages.entities.contents.GeoContent;
import com.teamagam.gimelgimel.domain.messages.entities.interfaces.visitor.IMessageVisitor;

/**
 * A class for geo messages (using GeoContent as its content)
 */
public class MessageGeo extends Message<GeoContent>{

    public MessageGeo(String senderId, GeoContent location) {
        super(senderId, GEO);
        mContent = location;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}



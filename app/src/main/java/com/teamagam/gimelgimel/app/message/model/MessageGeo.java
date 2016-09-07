package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.model.ViewsModels.IMessageVisitor;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;

/**
 * A class for geo messages (using GeoContentData as its content)
 */
public class MessageGeo extends Message<GeoContent> {

    public MessageGeo(String senderId, GeoContent location) {
        super(senderId, Message.GEO);
        content = location;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}



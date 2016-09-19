package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.model.ViewsModels.IMessageVisitor;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;

/**
 * A class for geo messages (using GeoContentData as its content)
 */
public class MessageGeoModel extends Message<GeoContent> {

    public MessageGeoModel(GeoContent location) {
        super(null, Message.GEO);
        mContent = location;
    }

    public MessageGeoModel(String senderId, GeoContent location) {
        super(senderId, Message.GEO);
        mContent = location;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}



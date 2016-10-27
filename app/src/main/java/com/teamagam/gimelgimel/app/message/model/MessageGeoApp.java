package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;

/**
 * A class for geo messages (using GeoContentData as its content)
 */
public class MessageGeoApp extends MessageApp<GeoContentApp> {

    public MessageGeoApp(GeoContentApp location) {
        super(null, MessageApp.GEO);
        mContent = location;
    }

    public MessageGeoApp(String senderId, GeoContentApp location) {
        super(senderId, MessageApp.GEO);
        mContent = location;
    }

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}



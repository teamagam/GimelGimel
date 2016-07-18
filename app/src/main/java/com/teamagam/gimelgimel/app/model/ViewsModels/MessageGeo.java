package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.GeoTextSample;

/**
 * A class for geo messages (using GeoTextSample as its content)
 */
public class MessageGeo extends Message<GeoTextSample>{

    public MessageGeo(String senderId, GeoTextSample location) {
        super(senderId, Message.GEO);
        mContent = location;
    }
}



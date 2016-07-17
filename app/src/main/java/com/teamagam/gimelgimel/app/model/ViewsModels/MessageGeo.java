package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.GeoTextSample;

/**
 * Created by Gil.Raytan on 10-Jul-16.
 * A class for geo messages (using LocationEntity as its content)
 */
public class MessageGeo extends Message<GeoTextSample>{

    public MessageGeo(String senderId, GeoTextSample location) {
        super(senderId, Message.GEO);
        mContent = location;
    }
}



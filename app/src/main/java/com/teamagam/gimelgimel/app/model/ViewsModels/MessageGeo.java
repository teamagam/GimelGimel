package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.model.entities.LocationEntity;

/**
 * Created by Gil.Raytan on 10-Jul-16.
 */
public class MessageGeo extends Message<LocationEntity>{

    public MessageGeo(String senderId, LocationEntity location) {
        super(senderId, Message.GEO);
        mContent = location;
    }
}



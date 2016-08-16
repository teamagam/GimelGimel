package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;

/**
 * A class for geo messages (using GeoContentData as its content)
 */
public class MessageGeoData extends MessageData<GeoContentData>{

    public MessageGeoData(String senderId, GeoContentData location) {
        super(senderId, MessageData.GEO);
        mContent = location;
    }

}



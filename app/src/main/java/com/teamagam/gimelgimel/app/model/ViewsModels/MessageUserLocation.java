package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.LocationSample;

/**
 * UserLocation-Type class for {@link Message}'s inner content
 */
public class MessageUserLocation extends Message<LocationSample> {

    public MessageUserLocation(String senderId, LocationSample sample) {
        super(senderId, Message.USER_LOCATION);
        mContent = sample;
    }
}

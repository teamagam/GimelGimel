package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

/**
 * UserLocation-Type class for {@link MessageData}'s inner content
 */
public class MessageUserLocationData extends MessageData<LocationSample> {

    public MessageUserLocationData(String senderId, LocationSample sample) {
        super(senderId, MessageData.USER_LOCATION);
        mContent = sample;
    }

}

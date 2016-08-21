package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;

/**
 * UserLocation-Type class for {@link MessageData}'s inner content
 */
public class MessageUserLocationData extends MessageData<LocationSampleData> {

    public MessageUserLocationData(LocationSampleData sample) {
        super(MessageData.USER_LOCATION);
        mContent = sample;
    }

}

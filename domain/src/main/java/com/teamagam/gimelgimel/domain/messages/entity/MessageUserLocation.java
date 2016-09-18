package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

/**
 * UserLocation-Type class for {@link Message}
 */
public class MessageUserLocation extends Message {

    private LocationSampleEntity mLocationSampleEntity;

    public MessageUserLocation(String senderId, LocationSampleEntity sample) {
        super(senderId);

        mLocationSampleEntity = sample;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public LocationSampleEntity getLocationSample() {
        return mLocationSampleEntity;
    }
}

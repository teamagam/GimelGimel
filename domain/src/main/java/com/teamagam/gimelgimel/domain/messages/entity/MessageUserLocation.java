package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

/**
 * UserLocation-Type class for {@link Message}
 */
public class MessageUserLocation extends Message {

    private LocationSample mLocationSample;

    public MessageUserLocation(String senderId, LocationSample sample) {
        super(senderId);

        mLocationSample = sample;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public LocationSample getLocationSample() {
        return mLocationSample;
    }
}

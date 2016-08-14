package com.gimelgimel.domain.model;

import com.gimelgimel.domain.model.contents.LocationSample;
import com.gimelgimel.domain.model.visitor.IMessageVisitor;

/**
 * UserLocation-Type class for {@link MessageModel}
 */
public class MessageUserLocation extends MessageModel {

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

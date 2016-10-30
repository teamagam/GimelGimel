package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

/**
 * UserLocation-Type class for {@link Message}
 */
public class MessageUserLocation extends Message {

    private LocationSampleEntity mLocationSampleEntity;

    public MessageUserLocation(String messageId, String senderId, Date createdAt, LocationSampleEntity sample) {
        super(messageId, senderId, createdAt);

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

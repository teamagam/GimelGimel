package com.teamagam.gimelgimel.domain.messages.entities;


import com.teamagam.gimelgimel.domain.messages.entities.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entities.interfaces.visitor.IMessageVisitor;

/**
 * UserLocation-Type class for {@link Message}'s inner content
 */
public class MessageUserLocation extends Message<LocationSample> {

    public MessageUserLocation(String senderId, LocationSample sample) {
        super(senderId, USER_LOCATION);
        mContent = sample;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}

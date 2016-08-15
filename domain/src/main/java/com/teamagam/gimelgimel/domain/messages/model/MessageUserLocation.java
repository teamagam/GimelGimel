package com.teamagam.gimelgimel.domain.messages.model;

/**
 * UserLocation-Type class for {@link MessageModel}
 */
public class MessageUserLocation extends MessageModel {

    private com.teamagam.gimelgimel.domain.messages.model.contents.LocationSample mLocationSample;

    public MessageUserLocation(String senderId, com.teamagam.gimelgimel.domain.messages.model.contents.LocationSample sample) {
        super(senderId);

        mLocationSample = sample;
    }

    @Override
    public void accept(com.teamagam.gimelgimel.domain.messages.model.visitor.IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public com.teamagam.gimelgimel.domain.messages.model.contents.LocationSample getLocationSample() {
        return mLocationSample;
    }
}

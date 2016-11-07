package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;
import com.teamagam.gimelgimel.app.message.model.contents.LocationSample;

/**
 * UserLocation-Type class for {@link MessageApp}'s inner content
 */
public class MessageUserLocationApp extends MessageApp<LocationSample> {

    public MessageUserLocationApp(LocationSample sample) {
        super(MessageApp.USER_LOCATION);
        mContent = sample;
    }

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

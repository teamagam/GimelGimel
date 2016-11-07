package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;

/**
 * UserLocation-Type class for {@link MessageData}'s inner content
 */
public class MessageUserLocationData extends MessageData<LocationSampleData> {

    public MessageUserLocationData(LocationSampleData sample) {
        super(MessageData.USER_LOCATION);
        mContent = sample;
    }

    @Override
    public void accept(IMessageDataVisitor visitor) {
        visitor.visit(this);
    }

}

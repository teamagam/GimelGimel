package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.contents.VectorLayerData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;

public class MessageVectorLayerData extends MessageData<VectorLayerData> {

    public MessageVectorLayerData() {
        super(MessageData.VECTOR_LAYER);
    }

    @Override
    public void accept(IMessageDataVisitor visitor) {
        visitor.visit(this);
    }
}

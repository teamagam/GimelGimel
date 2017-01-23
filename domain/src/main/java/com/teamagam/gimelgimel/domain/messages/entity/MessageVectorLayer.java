package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageVectorLayer extends Message {

    private final VectorLayer mVectorLayer;

    public MessageVectorLayer(String messageId, String senderId, Date createdAt,
                              VectorLayer vectorLayer) {
        super(messageId, senderId, createdAt);
        mVectorLayer = vectorLayer;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public VectorLayer getVectorLayer() {
        return mVectorLayer;
    }
}

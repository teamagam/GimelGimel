package com.teamagam.gimelgimel.domain.messages.model;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageModel}
 */
public class MessageImage extends MessageModel {

    private com.teamagam.gimelgimel.domain.messages.model.contents.ImageMetadata mImageMetadata;

    public MessageImage(String senderId, com.teamagam.gimelgimel.domain.messages.model.contents.ImageMetadata metadata) {
        super(senderId);
        mImageMetadata = metadata;
    }

    @Override
    public void accept(com.teamagam.gimelgimel.domain.messages.model.visitor.IMessageVisitor visitor) {
        visitor.visit(this);
    }
}

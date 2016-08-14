package com.gimelgimel.domain.model;

import com.gimelgimel.domain.model.contents.ImageMetadata;
import com.gimelgimel.domain.model.visitor.IMessageVisitor;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageModel}
 */
public class MessageImage extends MessageModel{

    private ImageMetadata mImageMetadata;

    public MessageImage(String senderId, ImageMetadata metadata) {
        super(senderId);
        mImageMetadata = metadata;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}

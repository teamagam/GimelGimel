package com.teamagam.gimelgimel.domain.messages.entities;


import com.teamagam.gimelgimel.domain.messages.entities.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entities.interfaces.visitor.IMessageVisitor;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link Message}'s inner content
 */
public class MessageImage extends Message<ImageMetadata>{

    public MessageImage(String senderId, ImageMetadata meta) {
        super(senderId, IMAGE);
        mContent = meta;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}

package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link Message}'s inner content
 */
public class MessageImage extends Message<ImageMetadata>{

    public MessageImage(String senderId, ImageMetadata meta) {
        super(senderId, Message.IMAGE);
        content = meta;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}

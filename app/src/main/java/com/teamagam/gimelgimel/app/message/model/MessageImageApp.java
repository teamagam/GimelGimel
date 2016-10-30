package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadata;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageApp}'s inner content
 */
public class MessageImageApp extends MessageApp<ImageMetadata> {

    public MessageImageApp(ImageMetadata meta) {
        super(MessageApp.IMAGE);
        mContent = meta;
    }

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

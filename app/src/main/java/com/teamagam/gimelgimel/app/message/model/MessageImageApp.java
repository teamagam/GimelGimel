package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageApp}'s inner content
 */
public class MessageImageApp extends MessageApp<ImageMetadataApp> {

    public MessageImageApp(ImageMetadataApp meta) {
        super(MessageApp.IMAGE);
        mContent = meta;
    }

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

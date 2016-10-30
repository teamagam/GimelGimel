package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;

/**
 * Text-Type class for {@link MessageApp}'s inner content
 */
public class MessageTextApp extends MessageApp<String> {

    public MessageTextApp(String text) {
        super(MessageApp.TEXT);
        mContent = text;
    }

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

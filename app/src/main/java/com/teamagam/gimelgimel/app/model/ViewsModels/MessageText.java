package com.teamagam.gimelgimel.app.model.ViewsModels;

/**
 * Text-Type class for {@link Message}'s inner content
 */
public class MessageText extends Message<String> {

    public MessageText(String senderId, String text) {
        super(senderId, Message.TEXT);
        mContent = text;
    }
}

package com.teamagam.gimelgimel.data.message.entity;

/**
 * Text-Type class for {@link MessageData}'s inner content
 */
public class MessageTextData extends MessageData<String> {

    public MessageTextData(String text) {
        super(MessageData.TEXT);
        mContent = text;
    }

}

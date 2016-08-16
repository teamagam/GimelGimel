package com.teamagam.gimelgimel.data.message.entity;

/**
 * Text-Type class for {@link MessageData}'s inner content
 */
public class MessageTextData extends MessageData<String> {

    public MessageTextData(String senderId, String text) {
        super(senderId, MessageData.TEXT);
        mContent = text;
    }

}

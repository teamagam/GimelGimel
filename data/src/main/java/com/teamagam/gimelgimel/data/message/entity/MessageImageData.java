package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageData}'s inner content
 */
public class MessageImageData extends MessageData<ImageMetadata>{

    public MessageImageData(String senderId, ImageMetadata meta) {
        super(senderId, MessageData.IMAGE);
        mContent = meta;
    }
}

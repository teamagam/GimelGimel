package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageData}'s inner content
 */
public class MessageImageData extends MessageData<ImageMetadataData>{

    public MessageImageData(ImageMetadataData meta) {
        super(MessageData.IMAGE);
        mContent = meta;
    }
}

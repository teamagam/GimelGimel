package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageData}'s inner content
 */
public class MessageImageData extends MessageData<ImageMetadataData>{

    public MessageImageData(ImageMetadataData meta) {
        super(MessageData.IMAGE);
        mContent = meta;
    }

    @Override
    public void accept(IMessageDataVisitor visitor) {
        visitor.visit(this);
    }

}

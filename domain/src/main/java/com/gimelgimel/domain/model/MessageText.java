package com.gimelgimel.domain.model;

import com.gimelgimel.domain.model.visitor.IMessageVisitor;

/**
 * Text-Type class for {@link MessageModel}
 */
public class MessageText extends MessageModel {

    private String mText;

    public MessageText(String senderId, String text) {
        super(senderId);
        mText = text;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return mText;
    }
}

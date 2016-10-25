package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

/**
 * Text-Type class for {@link Message}
 */
public class MessageText extends Message {

    private String mText;

    public MessageText(String messageId, String senderId, String text) {
        super(messageId, senderId);
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

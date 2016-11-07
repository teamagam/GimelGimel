package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

/**
 * Text-Type class for {@link Message}
 */
public class MessageText extends Message {

    private String mText;

    public MessageText(String messageId, String senderId, Date createdAt,
                       boolean isRead, boolean isSelected, String text) {
        super(messageId, senderId, createdAt, isRead, isSelected);
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

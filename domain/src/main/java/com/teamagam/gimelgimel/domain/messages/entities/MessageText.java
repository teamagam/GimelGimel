package com.teamagam.gimelgimel.domain.messages.entities;

import com.teamagam.gimelgimel.domain.messages.entities.interfaces.visitor.IMessageVisitor;

/**
 * Text-Type class for {@link Message}'s inner content
 */
public class MessageText extends Message<String> {

    public MessageText(String senderId, String text) {
        super(senderId, TEXT);
        mContent = text;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }
}

package com.teamagam.gimelgimel.domain.messages.model;

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
    public void accept(com.teamagam.gimelgimel.domain.messages.model.visitor.IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return mText;
    }
}

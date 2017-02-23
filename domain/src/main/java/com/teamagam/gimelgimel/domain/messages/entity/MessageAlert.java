package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageAlert extends Message {

    private final Alert mAlert;

    public MessageAlert(String messageId, String senderId, Date createdAt, Alert alert) {
        super(messageId, senderId, createdAt);
        mAlert = alert;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public Alert getAlert() {
        return mAlert;
    }
}

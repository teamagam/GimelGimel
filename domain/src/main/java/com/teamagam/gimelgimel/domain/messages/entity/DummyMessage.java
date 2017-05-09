package com.teamagam.gimelgimel.domain.messages.entity;


import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class DummyMessage extends Message {

    private static String EMPTY = "";

    public DummyMessage(Date createdAt) {
        super(EMPTY, EMPTY, createdAt);
    }

    @Override
    public void accept(IMessageVisitor visitor) {
    }
}

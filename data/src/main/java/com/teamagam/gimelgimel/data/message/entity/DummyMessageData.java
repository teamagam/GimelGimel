package com.teamagam.gimelgimel.data.message.entity;


import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;

import java.util.Date;

public class DummyMessageData extends MessageData {
    public DummyMessageData(Date date) {
        super(DUMMY);
        setCreatedAt(date);
    }

    @Override
    public void accept(IMessageDataVisitor visitor) {
        visitor.visit(this);
    }
}

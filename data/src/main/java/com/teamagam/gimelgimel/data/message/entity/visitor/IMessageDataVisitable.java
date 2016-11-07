package com.teamagam.gimelgimel.data.message.entity.visitor;

public interface IMessageDataVisitable {
    void accept(IMessageDataVisitor visitor);
}

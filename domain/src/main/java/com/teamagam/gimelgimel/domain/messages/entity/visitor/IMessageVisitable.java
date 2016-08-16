package com.teamagam.gimelgimel.domain.messages.entity.visitor;

public interface IMessageVisitable {
    void accept(IMessageVisitor visitor);
}

package com.teamagam.gimelgimel.domain.messages.entities.interfaces.visitor;

public interface IMessageVisitable {
    void accept(IMessageVisitor visitor);
}

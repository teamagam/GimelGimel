package com.teamagam.gimelgimel.domain.messages.model.visitor;

public interface IMessageVisitable {
    void accept(IMessageVisitor visitor);
}

package com.gimelgimel.domain.model.visitor;

public interface IMessageVisitable {
    void accept(IMessageVisitor visitor);
}

package com.teamagam.gimelgimel.app.message.model.visitor;

public interface IMessageAppVisitable {
    void accept(IMessageAppVisitor visitor);
}

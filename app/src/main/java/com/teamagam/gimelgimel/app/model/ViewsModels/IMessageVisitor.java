package com.teamagam.gimelgimel.app.model.ViewsModels;

public interface IMessageVisitor {
    void visit(MessageUserLocation message);
    void visit(MessageGeo message);
    void visit(MessageText message);
    void visit(MessageImage message);
}

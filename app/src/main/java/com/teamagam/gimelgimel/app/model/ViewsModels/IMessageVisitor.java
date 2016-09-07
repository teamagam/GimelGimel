package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.message.model.MessageGeo;

public interface IMessageVisitor {
    void visit(MessageUserLocation message);
    void visit(MessageGeo message);
    void visit(MessageText message);
    void visit(MessageImage message);
}

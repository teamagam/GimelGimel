package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.message.model.MessageGeoModel;

public interface IMessageVisitor {
    void visit(MessageUserLocation message);
    void visit(MessageGeoModel message);
    void visit(MessageText message);
    void visit(MessageImage message);
}

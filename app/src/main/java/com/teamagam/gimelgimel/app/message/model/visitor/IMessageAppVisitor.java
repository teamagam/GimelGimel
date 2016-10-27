package com.teamagam.gimelgimel.app.message.model.visitor;

import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.MessageUserLocationApp;

public interface IMessageAppVisitor {
    void visit(MessageUserLocationApp message);
    void visit(MessageGeoApp message);
    void visit(MessageTextApp message);
    void visit(MessageImageApp message);
}

package com.teamagam.gimelgimel.domain.messages.model.visitor;

import com.teamagam.gimelgimel.domain.messages.model.MessageImage;
import com.teamagam.gimelgimel.domain.messages.model.MessageText;
import com.teamagam.gimelgimel.domain.messages.model.MessageUserLocation;

public interface IMessageVisitor {
    void visit(MessageUserLocation message);
    void visit(com.teamagam.gimelgimel.domain.messages.model.MessageGeo message);
    void visit(MessageText message);
    void visit(MessageImage message);
}

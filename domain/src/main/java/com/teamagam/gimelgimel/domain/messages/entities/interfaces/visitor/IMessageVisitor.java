package com.teamagam.gimelgimel.domain.messages.entities.interfaces.visitor;

import com.teamagam.gimelgimel.domain.messages.entities.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entities.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entities.MessageText;
import com.teamagam.gimelgimel.domain.messages.entities.MessageUserLocation;

public interface IMessageVisitor {
    void visit(MessageUserLocation message);
    void visit(MessageGeo message);
    void visit(MessageText message);
    void visit(MessageImage message);
}

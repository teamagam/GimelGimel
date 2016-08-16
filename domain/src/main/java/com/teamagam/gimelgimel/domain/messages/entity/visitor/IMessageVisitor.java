package com.teamagam.gimelgimel.domain.messages.entity.visitor;

import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;

public interface IMessageVisitor {
    void visit(com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation message);
    void visit(MessageGeo message);
    void visit(MessageText message);
    void visit(MessageImage message);
}

package com.gimelgimel.domain.model.visitor;

import com.gimelgimel.domain.model.MessageGeo;
import com.gimelgimel.domain.model.MessageImage;
import com.gimelgimel.domain.model.MessageText;
import com.gimelgimel.domain.model.MessageUserLocation;

public interface IMessageVisitor {
    void visit(MessageUserLocation message);
    void visit(MessageGeo message);
    void visit(MessageText message);
    void visit(MessageImage message);
}

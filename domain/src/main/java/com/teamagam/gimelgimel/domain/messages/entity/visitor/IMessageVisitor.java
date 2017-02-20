package com.teamagam.gimelgimel.domain.messages.entity.visitor;

import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageSensor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.MessageVectorLayer;

public interface IMessageVisitor {
    void visit(MessageUserLocation message);

    void visit(MessageGeo message);

    void visit(MessageText message);

    void visit(MessageImage message);

    void visit(MessageSensor message);

    void visit(MessageAlert message);

    void visit(MessageVectorLayer message);
}

package com.teamagam.gimelgimel.data.message.entity.visitor;


import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;

public interface IMessageDataVisitor {
    void visit(MessageUserLocationData message);
    void visit(MessageGeoData message);
    void visit(MessageTextData message);
    void visit(MessageImageData message);
}

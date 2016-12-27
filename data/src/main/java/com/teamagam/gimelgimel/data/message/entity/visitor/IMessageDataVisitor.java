package com.teamagam.gimelgimel.data.message.entity.visitor;


import com.teamagam.gimelgimel.data.message.entity.MessageAlertData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageSensorData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.MessageVectorLayerData;

public interface IMessageDataVisitor {
    void visit(MessageUserLocationData message);

    void visit(MessageGeoData message);

    void visit(MessageTextData message);

    void visit(MessageImageData message);

    void visit(MessageSensorData message);

    void visit(MessageVectorLayerData message);

    void visit(MessageAlertData messageAlertData);
}

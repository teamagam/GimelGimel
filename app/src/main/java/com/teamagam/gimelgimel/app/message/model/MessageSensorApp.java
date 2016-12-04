package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.contents.SensorMetadataApp;
import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;

public class MessageSensorApp extends MessageApp<SensorMetadataApp> {

    public MessageSensorApp(SensorMetadataApp sensorMetadataApp) {
        super(MessageApp.SENSOR);
        mContent = sensorMetadataApp;
    }

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

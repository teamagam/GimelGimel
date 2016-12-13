package com.teamagam.gimelgimel.app.sensor.model;

import com.teamagam.gimelgimel.app.message.model.MessageApp;
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

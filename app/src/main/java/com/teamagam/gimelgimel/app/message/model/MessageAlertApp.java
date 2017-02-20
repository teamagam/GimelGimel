package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;
import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;

public class MessageAlertApp extends MessageApp<GeoAlert> {

    public MessageAlertApp(GeoAlert alert) {
        super(MessageApp.ALERT);
        mContent = alert;
    }

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

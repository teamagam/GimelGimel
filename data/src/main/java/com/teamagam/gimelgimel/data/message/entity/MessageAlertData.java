package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.alerts.entity.AlertData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;

public class MessageAlertData extends MessageData<AlertData>{

    public MessageAlertData(AlertData content) {
        super(MessageData.ALERT);
        mContent = content;
    }

    @Override
    public void accept(IMessageDataVisitor visitor) {
        visitor.visit(this);
    }


}

package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.message.model.MessageApp;

/**
 * Model's repository for holding messages read-state data
 */
public interface MessagesReadStatusModel extends DataChangedObservable {
    void markAsRead(MessageApp message);

    boolean isRead(MessageApp message);

    int getCount();
}

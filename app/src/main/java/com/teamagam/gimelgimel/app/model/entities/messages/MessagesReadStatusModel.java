package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Model's repository for holding messages read-state data
 */
public interface MessagesReadStatusModel extends DataChangedObservable {
    void markAsRead(Message message);

    boolean isRead(Message message);
}

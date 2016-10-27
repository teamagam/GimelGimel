package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.message.model.MessageApp;

/**
 * Data access interface to model's selected message
 */
public interface SelectedMessageModel extends DataChangedObservable{

    void select(MessageApp message);

    MessageApp getSelected();

    boolean isAnySelected();
}

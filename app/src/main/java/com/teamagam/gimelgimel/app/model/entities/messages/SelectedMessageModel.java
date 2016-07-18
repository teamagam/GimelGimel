package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Data access interface to model's selected message
 */
public interface SelectedMessageModel extends DataChangedObservable{

    void select(Message message);

    Message getSelected();

    boolean isAnySelected();
}

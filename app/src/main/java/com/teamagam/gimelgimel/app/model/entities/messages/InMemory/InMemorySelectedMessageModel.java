package com.teamagam.gimelgimel.app.model.entities.messages.InMemory;

import com.teamagam.gimelgimel.app.common.NotifyingDataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

/**
 * In-Memory implementation of {@link SelectedMessageModel}.
 * Meaning, the selected message data will be kept for as long as the app lives
 */
public class InMemorySelectedMessageModel extends NotifyingDataChangedObservable implements SelectedMessageModel {

    private Message mSelected;

    @Override
    public void select(Message message) {
        mSelected = message;
        notifyObservers();
    }

    @Override
    public Message getSelected() {
        return mSelected;
    }

    @Override
    public boolean isAnySelected() {
        return mSelected != null;
    }
}

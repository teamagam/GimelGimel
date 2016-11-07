package com.teamagam.gimelgimel.app.model.entities.messages.InMemory;

import com.teamagam.gimelgimel.app.common.NotifyingDataChangedObservable;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

import javax.inject.Inject;

/**
 * In-Memory implementation of {@link SelectedMessageModel}.
 * Meaning, the selected message data will be kept for as long as the app lives
 */
@PerActivity
public class InMemorySelectedMessageModel extends NotifyingDataChangedObservable implements SelectedMessageModel {

    private MessageApp mSelected;

    @Inject
    public InMemorySelectedMessageModel() {
    }

    @Override
    public void select(MessageApp message) {
        mSelected = message;
        notifyObservers();
    }

    @Override
    public MessageApp getSelected() {
        return mSelected;
    }

    @Override
    public boolean isAnySelected() {
        return mSelected != null;
    }
}

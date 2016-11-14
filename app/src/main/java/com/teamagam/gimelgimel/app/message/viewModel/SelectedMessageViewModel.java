package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.app.viewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.SyncSelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.Date;

import javax.inject.Inject;

/**
 * Shared functionality of selected message view-model.
 * Verifies that a message is selected before querying for any selected-message data.
 * Notifies observers on data changed events if an abstract condition is met (implemented by extending classes)
 */
public abstract class SelectedMessageViewModel<V> extends BaseViewModel<V>{

    MessageApp mMessageSelected;

    @Inject
    MessageAppMapper mTransformer;

    @Inject
    SyncSelectedMessageInteractorFactory syncSelectedMessageInteractorFactory;

    private SyncInteractor mSyncSelectedMessageInteractor;

    @Override
    public void stop() {
        super.stop();
        mSyncSelectedMessageInteractor.unsubscribe();
    }

    @Override
    public void start() {
        super.start();
        mSyncSelectedMessageInteractor = syncSelectedMessageInteractorFactory.create(new SelectedMessageDataSubscriber());
        mSyncSelectedMessageInteractor.execute();
    }

    public boolean isAnyMessageSelected() {
        return mMessageSelected != null;
    }

    protected String getType() {
        return mMessageSelected.getType();
    }

    protected String getSenderId() {
        return mMessageSelected.getSenderId();
    }

    protected Date getDate() {
        return mMessageSelected.getCreatedAt();
    }

    protected void updateSelectedMessage() {
        //no-op if not needed.
    }

    protected abstract boolean shouldNotifyOnSelectedMessage();

    private class SelectedMessageDataSubscriber extends SimpleSubscriber<Message>  {

        @Override
        public void onNext(Message message) {
            mMessageSelected = mTransformer.transformToModel(message);
            if (shouldNotifyOnSelectedMessage()) {
                updateSelectedMessage();
                notifyChange();
            }
        }

    }
}

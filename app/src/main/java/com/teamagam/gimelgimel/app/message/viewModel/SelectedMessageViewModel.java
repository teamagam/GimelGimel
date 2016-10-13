package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageModelMappper;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.viewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.SyncSelectedMessageInteractorFactory;

import java.util.Date;

import javax.inject.Inject;

/**
 * Shared functionality of selected message view-model.
 * Verifies that a message is selected before querying for any selected-message data.
 * Notifies observers on data changed events if an abstract condition is met (implemented by extending classes)
 */
public abstract class SelectedMessageViewModel<V> extends BaseViewModel<V>{

    Message mMessageSelected;

    @Inject
    MessageModelMappper mTransformer;

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

    private class SelectedMessageDataSubscriber extends SimpleSubscriber<com.teamagam.gimelgimel.domain.messages.entity.Message>  {

        @Override
        public void onNext(com.teamagam.gimelgimel.domain.messages.entity.Message message) {
            mMessageSelected = mTransformer.transformToModel(message);
            if (shouldNotifyOnSelectedMessage()) {
                updateSelectedMessage();
                notifyChange();
            }
        }

    }
}

package com.teamagam.gimelgimel.app.message.viewModel;

import android.support.v7.widget.RecyclerView;

import com.teamagam.gimelgimel.app.common.DataRandomAccessor;
import com.teamagam.gimelgimel.app.message.view.MessagesMasterFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessagesRecyclerViewAdapter;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.GetMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.GetMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncMessagesInteractorFactory;

import javax.inject.Inject;

/**
 * Messages view-model for messages presentation use-case
 */
public class MessagesMasterViewModel extends SelectedMessageViewModel<MessagesMasterFragment>
        implements MessagesRecyclerViewAdapter.OnItemClickListener {

    private MessagesModel mMessagesModel;

    @Inject
    SyncMessagesInteractorFactory syncMessagesInteractorFactory;

    @Inject
    GetMessagesInteractorFactory getMessagesInteractorFactory;

    @Inject
    SelectMessageInteractorFactory selectMessageInteractorFactory;

    private SyncInteractor mSyncMessagesInteractor;
    private MessagesRecyclerViewAdapter mAdapter;
    private GetMessagesInteractor getMessagesInteractor;

    @Inject
    public MessagesMasterViewModel() {
        mMessagesModel = new InMemoryMessagesModel();
        mAdapter = new MessagesRecyclerViewAdapter(mMessagesModel, this);
    }

    @Override
    public void start() {
        super.start();

        getMessagesInteractor = getMessagesInteractorFactory.create(new GetMessagesSubscriber());
        getMessagesInteractor.execute();

        mSyncMessagesInteractor = syncMessagesInteractorFactory.create(
                new SimpleSubscriber<com.teamagam.gimelgimel.domain.messages.entity.Message>() {
                    @Override
                    public void onNext(com.teamagam.gimelgimel.domain.messages.entity.Message message) {
                        mMessagesModel.removeAll();
                        getMessagesInteractor.unsubscribe();
                        getMessagesInteractor = getMessagesInteractorFactory.create(new
                                GetMessagesSubscriber());
                        getMessagesInteractor.execute();
                    }
                }
        );
        mSyncMessagesInteractor.execute();
    }

    @Override
    protected boolean shouldNotifyOnSelectedMessage() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        getMessagesInteractor.unsubscribe();
        mSyncMessagesInteractor.unsubscribe();
    }

    public void select(MessageApp message) {
        selectMessageInteractorFactory.create(message.getMessageId()).execute();
    }

    @Override
    public void onListItemInteraction(MessageApp message) {
        sLogger.userInteraction("MessageApp [id=" + message.getSenderId() + "] clicked");
        select(message);
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public interface DisplayedMessagesRandomAccessor extends DataRandomAccessor<MessageApp> {
    }

    private class GetMessagesSubscriber extends SimpleSubscriber<com.teamagam.gimelgimel.domain
            .messages.entity.Message> {

        @Override
        public void onNext(com.teamagam.gimelgimel.domain.messages.entity.Message message) {
            mMessagesModel.add(mTransformer.transformToModel(message));
        }

        @Override
        public void onCompleted() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }
}

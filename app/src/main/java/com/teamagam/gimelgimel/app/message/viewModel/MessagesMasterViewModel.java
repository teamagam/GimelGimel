package com.teamagam.gimelgimel.app.message.viewModel;

import android.support.v7.widget.RecyclerView;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.base.adapters.DataRandomAccessor;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.view.MessagesMasterFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessagesRecyclerViewAdapter;
import com.teamagam.gimelgimel.domain.messages.DisplayMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import javax.inject.Inject;

/**
 * Messages view-model for messages presentation use-case
 */
public class MessagesMasterViewModel extends BaseViewModel<MessagesMasterFragment>
        implements MessagesRecyclerViewAdapter.OnItemClickListener {

    private static final AppLogger sLogger = AppLoggerFactory.create();

    @Inject
    SelectMessageInteractorFactory mSelectMessageInteractorFactory;

    @Inject
    DisplayMessagesInteractorFactory mDisplayMessagesInteractorFactory;

    @Inject
    MessageAppMapper mTransformer;

    private DisplayMessagesInteractor mDisplayMessagesInteractor;

    private MessagesRecyclerViewAdapter mAdapter;

    @Inject
    public MessagesMasterViewModel() {
        mAdapter = new MessagesRecyclerViewAdapter(new MyDisplayedMessagesRandomAccessor(), this);
    }

    @Override
    public void init() {
        super.init();
        mDisplayMessagesInteractor = mDisplayMessagesInteractorFactory.create(
                new DisplayMessagesInteractor.Displayer() {
                    @Override
                    public void show(Message message) {
                        MessageApp messageApp = mTransformer.transformToModel(message);
                        mAdapter.show(messageApp);
                    }

                    @Override
                    public void read(Message message) {
                        mAdapter.read(message.getMessageId());
                    }

                    @Override
                    public void select(Message message) {
                        sLogger.d("displayer select [id=" + message.getMessageId() + "]");
                        mAdapter.select(message.getMessageId());
                    }
                });
        mDisplayMessagesInteractor.execute();
    }


    @Override
    public void destroy() {
        super.destroy();
        //This should happen in onDestroy
        if (mDisplayMessagesInteractor != null) {
            mDisplayMessagesInteractor.unsubscribe();
        }
    }

    @Override
    public void onListItemInteraction(MessageApp message) {
        sLogger.userInteraction("MessageApp [id=" + message.getMessageId() + "] clicked");
        mSelectMessageInteractorFactory.create(message.getMessageId()).execute();
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public interface DisplayedMessagesRandomAccessor extends DataRandomAccessor<MessageApp> {
        void add(MessageApp messageApp);

        int getPosition(String messageId);
    }
}

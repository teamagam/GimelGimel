package com.teamagam.gimelgimel.app.message.viewModel;

import android.support.v7.widget.RecyclerView;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.RecyclerViewModel;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseDisplayedMessagesRandomAccessor;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessagesRecyclerViewAdapter;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ToggleMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplayMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

/**
 * Messages view-model for messages presentation use-case
 */
@AutoFactory
public class MessagesViewModel extends RecyclerViewModel
        implements MessagesRecyclerViewAdapter.OnItemClickListener<MessageApp> {

    DisplayMessagesInteractorFactory mDisplayMessagesInteractorFactory;
    SelectMessageInteractorFactory mSelectMessageInteractorFactory;
    DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
    MessageAppMapper mTransformer;

    private static final AppLogger sLogger = AppLoggerFactory.create();

    private DisplayMessagesInteractor mDisplayMessagesInteractor;
    private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;
    private MessagesRecyclerViewAdapter mAdapter;

    MessagesViewModel(@Provided GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
                      @Provided ToggleMessageOnMapInteractorFactory toggleMessageOnMapInteractorFactory,
                      @Provided DisplayMessagesInteractorFactory displayMessagesInteractorFactory,
                      @Provided SelectMessageInteractorFactory selectMessageInteractorFactory,
                      @Provided DisplaySelectedMessageInteractorFactory displaySelectedMessageInteractorFactory,
                      @Provided MessageAppMapper messageAppMapper) {
        mDisplayMessagesInteractorFactory = displayMessagesInteractorFactory;
        mSelectMessageInteractorFactory = selectMessageInteractorFactory;
        mDisplaySelectedMessageInteractorFactory = displaySelectedMessageInteractorFactory;
        mTransformer = messageAppMapper;

        mAdapter = new MessagesRecyclerViewAdapter(
                new BaseDisplayedMessagesRandomAccessor<MessageApp>(), this,
                goToLocationMapInteractorFactory, toggleMessageOnMapInteractorFactory);
    }

    @Override
    public void init() {
        super.init();
        mDisplayMessagesInteractor = mDisplayMessagesInteractorFactory.create(
                new MessageDisplayer());
        mDisplaySelectedMessageInteractor = mDisplaySelectedMessageInteractorFactory.create(
                new SelectedMessageDisplayer());

        mDisplayMessagesInteractor.execute();
        mDisplaySelectedMessageInteractor.execute();
    }

    @Override
    public void stop() {
        super.stop();

        if (mDisplayMessagesInteractor != null) {
            mDisplayMessagesInteractor.unsubscribe();
        }
        if (mDisplaySelectedMessageInteractor != null) {
            mDisplaySelectedMessageInteractor.unsubscribe();
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

    private class MessageDisplayer implements DisplayMessagesInteractor.Displayer {
        @Override
        public void show(Message message, boolean isFromSelf) {
            MessageApp messageApp = mTransformer.transformToModel(message, isFromSelf);
            mAdapter.show(messageApp);
        }

        @Override
        public void messageShownOnMap(Message message) {
            mAdapter.messageShownOnMap(message.getMessageId());
        }

        @Override
        public void messageHiddenFromMap(Message message) {
            mAdapter.messageHiddenFromMap(message.getMessageId());
        }
    }

    private class SelectedMessageDisplayer implements DisplaySelectedMessageInteractor.Displayer {
        @Override
        public void display(Message message) {
            sLogger.d("displayer select [id=" + message.getMessageId() + "]");

            try {

                int position = mAdapter.getItemPosition(message.getMessageId());
                ((MessagesContainerFragment) mView).scrollToPosition(position);

                mAdapter.select(message.getMessageId());
            } catch (Exception ignored) {

            }
        }
    }
}

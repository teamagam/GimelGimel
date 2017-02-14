package com.teamagam.gimelgimel.app.message.viewModel;

import android.support.v7.widget.RecyclerView;

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
import com.teamagam.gimelgimel.domain.messages.MessagePresentation;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import javax.inject.Inject;

/**
 * Messages view-model for messages presentation use-case
 */
public class MessagesViewModel extends RecyclerViewModel
        implements MessagesRecyclerViewAdapter.OnItemClickListener<MessageApp> {

    @Inject
    DisplayMessagesInteractorFactory mDisplayMessagesInteractorFactory;
    @Inject
    SelectMessageInteractorFactory mSelectMessageInteractorFactory;
    @Inject
    DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
    @Inject
    MessageAppMapper mTransformer;

    private static final AppLogger sLogger = AppLoggerFactory.create();

    private DisplayMessagesInteractor mDisplayMessagesInteractor;
    private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;
    private MessagesRecyclerViewAdapter mAdapter;

    @Inject
    MessagesViewModel(GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
                      ToggleMessageOnMapInteractorFactory toggleMessageOnMapInteractorFactory) {
        mAdapter = new MessagesRecyclerViewAdapter(
                this, goToLocationMapInteractorFactory, toggleMessageOnMapInteractorFactory);
    }

    @Override
    public void init() {
        super.init();
        mDisplayMessagesInteractor = mDisplayMessagesInteractorFactory.create(
                new MessageDisplayer());
        mDisplaySelectedMessageInteractor = mDisplaySelectedMessageInteractorFactory.create(
                new SelectedMessageDisplayer());
    }

    @Override
    public void start() {
        super.start();
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
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    private class MessageDisplayer implements DisplayMessagesInteractor.Displayer {
        @Override
        public void show(MessagePresentation message) {
            MessageApp messageApp = mTransformer.transformToModel(message.getMessage(),
                    message.isFromSelf(), message.isShownOnMap());
            mAdapter.show(messageApp);
        }
    }

    private class SelectedMessageDisplayer implements DisplaySelectedMessageInteractor.Displayer {
        @Override
        public void display(Message message) {
            sLogger.d("displayer select [id=" + message.getMessageId() + "]");

            int position = mAdapter.getItemPosition(message.getMessageId());
            ((MessagesContainerFragment) mView).scrollToPosition(position);

            mAdapter.select(message.getMessageId());
        }
    }
}
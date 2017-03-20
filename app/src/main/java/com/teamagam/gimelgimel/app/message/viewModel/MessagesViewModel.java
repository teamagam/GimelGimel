package com.teamagam.gimelgimel.app.message.viewModel;

import android.support.v7.widget.RecyclerView;

import com.teamagam.gimelgimel.app.common.base.ViewModels.RecyclerViewModel;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
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
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import javax.inject.Inject;

/**
 * Messages view-model for messages presentation use-case
 */
public class MessagesViewModel extends RecyclerViewModel<MessagesContainerFragment>
        implements MessagesRecyclerViewAdapter.OnItemClickListener<MessageApp>,
        MessagesRecyclerViewAdapter.OnNewDataListener<MessageApp>{

    @Inject
    DisplayMessagesInteractorFactory mDisplayMessagesInteractorFactory;
    @Inject
    DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
    @Inject
    UpdateMessagesReadInteractorFactory mUpdateMessagesReadInteractorFactory;
    @Inject
    MessageAppMapper mTransformer;

    private static final AppLogger sLogger = AppLoggerFactory.create();

    private DisplayMessagesInteractor mDisplayMessagesInteractor;
    private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;
    private MessagesRecyclerViewAdapter mAdapter;
    private boolean mIsScrollDownFabVisible;

    @Inject
    MessagesViewModel(GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
                      ToggleMessageOnMapInteractorFactory toggleMessageOnMapInteractorFactory,
                      GlideLoader glideLoader) {
        mAdapter = new MessagesRecyclerViewAdapter(this, this,
                goToLocationMapInteractorFactory, toggleMessageOnMapInteractorFactory, glideLoader);
        mIsScrollDownFabVisible = false;
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
        unsubscribe(mDisplayMessagesInteractor, mDisplaySelectedMessageInteractor);
    }

    @Override
    public void onListItemInteraction(MessageApp message) {
        sLogger.userInteraction("MessageApp [id=" + message.getMessageId() + "] clicked");
    }

    @Override
    public void onNewData(MessageApp messageApp) {
        if (!messageApp.isRead() && mView.isSlidingPanelOpen()) {
            indicateNewMessage(messageApp);
        }
    }

    public boolean isScrollDownFabVisible() {
        return mIsScrollDownFabVisible;
    }

    public void onScrollDownFabClicked() {
        scrollDown();
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void onLastVisibleItemPositionChanged(int position) {
        updateMessageReadTimestamp(position);
        updateScrollDownFabVisibility(position);
    }

    private void indicateNewMessage(MessageApp messageApp) {
        if (mView.isBeforeLastMessageVisible()) {
            scrollDown();
        } else if (!messageApp.isFromSelf()) {
            notifyNewMessage();
        }
    }

    private void scrollDown() {
        mView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void notifyNewMessage() {
        mView.displayNewMessageSnackbar(v -> scrollDown());
    }

    private void updateMessageReadTimestamp(int position) {
        MessageApp messageApp = mAdapter.get(position);
        mUpdateMessagesReadInteractorFactory.create(messageApp.getCreatedAt()).execute();
    }

    private void updateScrollDownFabVisibility(int position) {
        if (position == getLastMessagePosition()) {
            setScrollDownFabVisibility(false);
        } else {
            setScrollDownFabVisibility(true);
        }
    }

    private int getLastMessagePosition() {
        return mAdapter.getItemCount() - 1;
    }

    private void setScrollDownFabVisibility(boolean isVisible) {
        if (mIsScrollDownFabVisible != isVisible) {
            mIsScrollDownFabVisible = isVisible;
            notifyChange();
        }
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
            mView.scrollToPosition(position);

            mAdapter.select(message.getMessageId());
        }
    }
}
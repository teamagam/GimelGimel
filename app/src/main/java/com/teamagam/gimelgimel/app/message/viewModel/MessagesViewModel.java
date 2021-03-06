package com.teamagam.gimelgimel.app.message.viewModel;

import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.app.common.base.ViewModels.RecyclerViewModel;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessagesRecyclerViewAdapter;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ToggleMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplayMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.MessagePresentation;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.UpdateNewMessageIndicationDateFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesSearchInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesSearchInteractorFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.util.List;
import javax.inject.Inject;

public class MessagesViewModel extends RecyclerViewModel<MessagesContainerFragment>
    implements MessagesRecyclerViewAdapter.OnItemClickListener<MessagePresentation>,
    MessagesRecyclerViewAdapter.OnNewDataListener<MessagePresentation> {

  private static final AppLogger sLogger = AppLoggerFactory.create();
  @Inject
  DisplayMessagesInteractorFactory mDisplayMessagesInteractorFactory;
  @Inject
  DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
  @Inject
  UpdateMessagesReadInteractorFactory mUpdateMessagesReadInteractorFactory;
  @Inject
  UpdateNewMessageIndicationDateFactory mUpdateNewMessageIndicationDateFactory;
  @Inject
  MessagesSearchInteractorFactory mMessagesSearchInteractorFactory;

  private DisplayMessagesInteractor mDisplayMessagesInteractor;
  private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;
  private MessagesRecyclerViewAdapter mAdapter;
  private UserPreferencesRepository mUserPreferencesRepository;
  private boolean mIsScrollDownFabVisible;
  private boolean mIsSearchFabVisible;
  private SelectedMessageDisplayer mSelectedMessageDisplayer;
  private SearchResultsDisplayer mSearchResultsDisplayer;

  @Inject
  MessagesViewModel(GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      ToggleMessageOnMapInteractorFactory toggleMessageOnMapInteractorFactory,
      Navigator navigator,
      GlideLoader glideLoader,
      UserPreferencesRepository userPreferencesRepository,
      MessagesSearchInteractorFactory messagesSearchInteractorFactory) {
    mAdapter = new MessagesRecyclerViewAdapter(this, goToLocationMapInteractorFactory,
        toggleMessageOnMapInteractorFactory, glideLoader, navigator);
    mUserPreferencesRepository = userPreferencesRepository;
    mAdapter.setOnNewDataListener(this);
    mIsScrollDownFabVisible = false;
    mIsSearchFabVisible = true;
    mSearchResultsDisplayer = new SearchResultsDisplayer();
    mSelectedMessageDisplayer = new SelectedMessageDisplayer();
    mMessagesSearchInteractorFactory = messagesSearchInteractorFactory;
  }

  @Override
  public void init() {
    super.init();
    mDisplayMessagesInteractor = mDisplayMessagesInteractorFactory.create(new MessageDisplayer());
    mDisplaySelectedMessageInteractor =
        mDisplaySelectedMessageInteractorFactory.create(mSelectedMessageDisplayer);
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
  public void onListItemInteraction(MessagePresentation messagePresentation) {
    sLogger.userInteraction(
        "MessagePresentation [id=" + messagePresentation.getMessage().getMessageId() + "] clicked");
  }

  @Override
  public void onNewData(MessagePresentation messagePresentation) {
    if (!messagePresentation.isNotified() && mView.isSlidingPanelOpen()) {
      indicateNewMessage(messagePresentation);
    }
    updateIndicationDate(messagePresentation);
  }

  public boolean isScrollDownFabVisible() {
    return mIsScrollDownFabVisible;
  }

  @Bindable
  public boolean isSearchFabVisible() {
    return mIsSearchFabVisible;
  }

  public void onScrollDownFabClicked() {
    scrollDown();
  }

  public void onSearchDownFabClicked() {
    showSearchBox();
  }

  public void onNextResultSearchClicked() {
    mSearchResultsDisplayer.nextResult();
  }

  public void onPreviousResultSearchClicked() {
    mSearchResultsDisplayer.previousResult();
  }

  @Bindable
  public int getCurrentResultNumber() {
    return mSearchResultsDisplayer.currentResultNumber();
  }

  @Bindable
  public int getResultsAmount() {
    return mSearchResultsDisplayer.resultsAmount();
  }

  public void onEditSearchBoxResultClicked(CharSequence text) {
    mMessagesSearchInteractorFactory.create(mSearchResultsDisplayer, text.toString()).execute();
  }

  public RecyclerView.Adapter getAdapter() {
    return mAdapter;
  }

  public void onPanelOpened() {
    int lastVisibleItemPosition = mView.getLastVisibleItemPosition();
    if (lastVisibleItemPosition >= 0) {
      onLastVisibleItemPositionChanged(lastVisibleItemPosition);
    }
  }

  public void onLastVisibleItemPositionChanged(int position) {
    updateMessageReadTimestamp(position);
    updateScrollDownFabVisibility(position);
  }

  private void showSearchBox() {
    hideSearchButtonAndShowSearchBox();
  }

  private void hideSearchButtonAndShowSearchBox() {
    mIsSearchFabVisible = !mIsSearchFabVisible;
    notifyPropertyChanged(BR.searchFabVisible);
  }

  private void indicateNewMessage(MessagePresentation messagePresentation) {
    if (mView.isBeforeLastMessageVisible()) {
      scrollDown();
    } else if (!messagePresentation.isFromSelf()) {
      notifyNewMessage();
    }
  }

  private void scrollDown() {
    mView.scrollToPosition(mAdapter.getItemCount() - 1);
  }

  private void notifyNewMessage() {
    mView.displayNewMessageSnackbar(v -> scrollDown());
  }

  private void updateIndicationDate(MessagePresentation messagePresentation) {
    mUpdateNewMessageIndicationDateFactory.create(messagePresentation.getMessage().getCreatedAt())
        .execute();
  }

  private void updateMessageReadTimestamp(int position) {
    ChatMessage message = mAdapter.get(position).getMessage();
    mUpdateMessagesReadInteractorFactory.create(message.getCreatedAt(), getUsername(),
        message.getMessageId()).execute();
  }

  private String getUsername() {
    return mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY);
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
      mAdapter.show(message);
    }
  }

  private class SelectedMessageDisplayer implements DisplaySelectedMessageInteractor.Displayer {
    @Override
    public void display(ChatMessage message) {
      sLogger.d("displayer select [id=" + message.getMessageId() + "]");

      int position = mAdapter.getItemPosition(message.getMessageId());
      mView.scrollToPosition(position);

      mAdapter.select(message.getMessageId());
    }
  }

  private class SearchResultsDisplayer implements MessagesSearchInteractor.Displayer {

    private List<ChatMessage> mSearchResultsList;
    private int mCurrentShownResultIndex;

    @Override
    public synchronized void displayResults(List<ChatMessage> results) {
      mSearchResultsList = results;
      mCurrentShownResultIndex = 0;
      if (hasResults()) {
        showMessage();
      }
      notifyPropertyChanged(BR.currentResultNumber);
      notifyPropertyChanged(BR.resultsAmount);
    }

    public void nextResult() {
      mCurrentShownResultIndex = (mCurrentShownResultIndex + 1) % getResultsCount();
      notifyPropertyChanged(BR.currentResultNumber);
      showMessage();
    }

    public void previousResult() {
      mCurrentShownResultIndex =
          (mCurrentShownResultIndex + (getResultsCount() - 1)) % getResultsCount();

      notifyPropertyChanged(BR.currentResultNumber);
      showMessage();
    }

    public int resultsAmount() {
      return mSearchResultsList == null ? 0 : getResultsCount();
    }

    public int currentResultNumber() {
      return !hasResults() ? 0 : mCurrentShownResultIndex + 1;
    }

    private boolean hasResults() {
      return mSearchResultsList != null && !mSearchResultsList.isEmpty();
    }

    private void showMessage() {
      ChatMessage chatMessageToDisplay = mSearchResultsList.get(mCurrentShownResultIndex);
      mSelectedMessageDisplayer.display(chatMessageToDisplay);
    }

    private int getResultsCount() {
      return mSearchResultsList != null ? mSearchResultsList.size() : 0;
    }
  }
}
package com.teamagam.gimelgimel.domain.messages.repository;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.search.MessagesTextSearcher;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;

@AutoFactory
public class MessagesSearchInteractor extends BaseSingleDisplayInteractor {
  private MessagesTextSearcher mMessagesTextSearcher;
  private Displayer mMessagesSearchResultsDisplayer;
  private String mSearchText;

  @Inject
  public MessagesSearchInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided MessagesTextSearcher messagesTextSearcher,
      MessagesSearchInteractor.Displayer messagesSearchResultsDisplayer,
      String searchText) {
    super(threadExecutor, postExecutionThread);
    mSearchText = searchText;
    mMessagesTextSearcher = messagesTextSearcher;
    mMessagesSearchResultsDisplayer = messagesSearchResultsDisplayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mMessagesTextSearcher.searchMessagesByText(mSearchText)),
        receiverObservable -> receiverObservable, mMessagesSearchResultsDisplayer::displayResults);
  }

  public interface Displayer {
    void displayResults(List<ChatMessage> results);
  }
}

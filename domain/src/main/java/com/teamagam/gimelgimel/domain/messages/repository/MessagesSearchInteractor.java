package com.teamagam.gimelgimel.domain.messages.repository;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;

@AutoFactory
public class MessagesSearchInteractor extends BaseSingleDataInteractor {
  private List<ChatMessage> mSearchResults;
  private Displayer mMessagesSearchResultsDisplayer;

  @Inject
  public MessagesSearchInteractor(@Provided ThreadExecutor threadExecutor,
      List<ChatMessage> searchResults,
      MessagesSearchInteractor.Displayer messagesSearchResultsDisplayer) {
    super(threadExecutor);
    mSearchResults = searchResults;
    mMessagesSearchResultsDisplayer = messagesSearchResultsDisplayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(0), receiverObservable -> receiverObservable.doOnNext(
        o -> mMessagesSearchResultsDisplayer.displayResults(mSearchResults)));
  }

  public interface Displayer {
    void displayResults(List<ChatMessage> results);

    void nextResult();

    void previousResult();
  }
}

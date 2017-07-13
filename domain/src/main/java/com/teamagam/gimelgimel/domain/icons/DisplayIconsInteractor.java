package com.teamagam.gimelgimel.domain.icons;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;
import io.reactivex.Observable;
import java.util.Collections;

@AutoFactory
public class DisplayIconsInteractor extends BaseDisplayInteractor {

  private IconsRepository mIconsRepository;
  private Displayer mDisplayer;

  public DisplayIconsInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided IconsRepository iconsRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);

    mIconsRepository = iconsRepository;
    mDisplayer = displayer;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return Collections.singleton(
        factory.createSimple(Observable.fromIterable(mIconsRepository.getAvailableIcons()),
            mDisplayer::display));
  }

  public interface Displayer {
    void display(Icon icon);
  }
}

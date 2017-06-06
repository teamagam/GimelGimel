package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.base.adapters.BottomPanelPagerAdapter;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityPanel;
import com.teamagam.gimelgimel.app.map.view.MapEntityDetailsFragment;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.domain.map.DisplayKmlEntityInfoInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayKmlEntityInfoInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectKmlEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import javax.inject.Inject;

@AutoFactory
public class PanelViewModel extends BaseViewModel<MainActivityPanel> {

  private static final int MESSAGES_CONTAINER_ID = 0;
  private static final int DETAILS_CONTAINER_ID = 2;

  private final Context mContext;
  private final FragmentManager mFragmentManager;
  private final SelectKmlEntityInteractorFactory mSelectKmlEntityInteractorFactory;
  private final DisplayUnreadMessagesCountInteractorFactory mDisplayUnreadCountInteractorFactory;
  private final DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
  private final DisplayKmlEntityInfoInteractorFactory mDisplayKmlEntityInfoInteractorFactory;
  protected AppLogger sLogger = AppLoggerFactory.create();
  private DisplayUnreadMessagesCountInteractor mDisplayUnreadMessagesCountInteractor;
  private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;
  private DisplayKmlEntityInfoInteractor mDisplayKmlEntityInfoInteractor;
  private BottomPanelPagerAdapter mPageAdapter;
  private int mCurrentlySelectedPageId;

  @Inject
  PanelViewModel(
      @Provided
          Context context,
      @Provided
          SelectKmlEntityInteractorFactory selectKmlEntityInteractorFactory,
      @Provided
          DisplayUnreadMessagesCountInteractorFactory displayUnreadMessagesCountInteractorFactory,
      @Provided
          DisplaySelectedMessageInteractorFactory displaySelectedMessageInteractorFactory,
      @Provided
          DisplayKmlEntityInfoInteractorFactory displayKmlEntityInfoInteractorFactory,
      FragmentManager fragmentManager) {
    mContext = context;
    mSelectKmlEntityInteractorFactory = selectKmlEntityInteractorFactory;
    mDisplayUnreadCountInteractorFactory = displayUnreadMessagesCountInteractorFactory;
    mDisplaySelectedMessageInteractorFactory = displaySelectedMessageInteractorFactory;
    mDisplayKmlEntityInfoInteractorFactory = displayKmlEntityInfoInteractorFactory;
    mFragmentManager = fragmentManager;
  }

  public static boolean isOpenState(SlidingUpPanelLayout.PanelState state) {
    return state == SlidingUpPanelLayout.PanelState.ANCHORED
        || state == SlidingUpPanelLayout.PanelState.EXPANDED;
  }

  @Override
  public void start() {
    super.start();
    mPageAdapter = new BottomPanelPagerAdapter(mFragmentManager);
    setInitialPages();
    mView.setAdapter(mPageAdapter);
    createDisplayInteractors();
    executeDisplayInteractors();
  }

  @Override
  public void stop() {
    super.stop();
    unsubscribeDisplayInteractors();
  }

  public void onPageSelected() {
    removeDetailsPageIfNeeded();
  }

  private void setInitialPages() {
    mPageAdapter.addPage(MESSAGES_CONTAINER_ID, getMessagesContainerTitle(0),
        new MessagesContainerFragmentFactory());
    updateCurrentlySelectedPageId();
  }

  private void createDisplayInteractors() {
    mDisplayUnreadMessagesCountInteractor =
        mDisplayUnreadCountInteractorFactory.create(new UnreadMessagesCountRenderer());
    mDisplaySelectedMessageInteractor =
        mDisplaySelectedMessageInteractorFactory.create(new SelectedMessageDisplayer());
    mDisplayKmlEntityInfoInteractor =
        mDisplayKmlEntityInfoInteractorFactory.create(new KmlEntityInfoDisplayer());
  }

  private void executeDisplayInteractors() {
    mDisplayUnreadMessagesCountInteractor.execute();
    mDisplaySelectedMessageInteractor.execute();
    mDisplayKmlEntityInfoInteractor.execute();
  }

  private void unsubscribeDisplayInteractors() {
    mDisplayUnreadMessagesCountInteractor.unsubscribe();
    mDisplaySelectedMessageInteractor.unsubscribe();
    mDisplayKmlEntityInfoInteractor.unsubscribe();
  }

  private void removeDetailsPageIfNeeded() {
    if (mCurrentlySelectedPageId == DETAILS_CONTAINER_ID) {
      removeDetailsPage();
      mSelectKmlEntityInteractorFactory.create(null).execute();
    }
  }

  private void removeDetailsPage() {
    mPageAdapter.removePage(DETAILS_CONTAINER_ID);
    updateCurrentlySelectedPageId();
  }

  private void updateCurrentlySelectedPageId() {
    mCurrentlySelectedPageId = mPageAdapter.getId(mView.getCurrentPagePosition());
  }

  private String getMessagesContainerTitle(int unreadMessagesCount) {
    return getMessagesTitle() + getMessagesCounterExtension(unreadMessagesCount);
  }

  private String getMessagesTitle() {
    return mContext.getString(R.string.messages_container_title);
  }

  private String getMessagesCounterExtension(int unreadMessagesCount) {
    if (unreadMessagesCount > 0) {
      return mContext.getString(R.string.bottom_panel_messages_counter, unreadMessagesCount);
    } else {
      return "";
    }
  }

  private String getMapEntityDetailsContainerTitle(String entityName) {
    return mContext.getString(R.string.map_entity_details_container_title, entityName);
  }

  private static class MessagesContainerFragmentFactory
      implements BottomPanelPagerAdapter.FragmentFactory {
    @Override
    public Fragment create() {
      return new MessagesContainerFragment();
    }
  }

  private static class MapEntityDetailsFragmentFactory
      implements BottomPanelPagerAdapter.FragmentFactory {
    @Override
    public Fragment create() {
      return new MapEntityDetailsFragment();
    }
  }

  private class SelectedMessageDisplayer implements DisplaySelectedMessageInteractor.Displayer {
    @Override
    public void display(Message message) {
      mView.changePanelPage(MESSAGES_CONTAINER_ID);
      mView.anchorSlidingPanel();
    }
  }

  private class UnreadMessagesCountRenderer
      implements DisplayUnreadMessagesCountInteractor.Renderer {
    @Override
    public void renderUnreadMessagesCount(int unreadMessagesCount) {
      mPageAdapter.updateTitle(MESSAGES_CONTAINER_ID,
          getMessagesContainerTitle(unreadMessagesCount));
    }
  }

  private class KmlEntityInfoDisplayer implements DisplayKmlEntityInfoInteractor.Displayer {

    @Override
    public void display(KmlEntityInfo kmlEntityInfo) {
      displayDetailsPage(kmlEntityInfo);
      mView.setCurrentPage(mPageAdapter.getPosition(DETAILS_CONTAINER_ID));
      updateCurrentlySelectedPageId();
    }

    @Override
    public void hide() {
      removeDetailsPage();
    }

    private void displayDetailsPage(KmlEntityInfo kmlEntityInfo) {
      if (mCurrentlySelectedPageId == DETAILS_CONTAINER_ID) {
        updateDetailsPage(kmlEntityInfo);
      } else {
        addDetailsPage(kmlEntityInfo);
      }
      updateCurrentlySelectedPageId();
    }

    private void updateDetailsPage(KmlEntityInfo kmlEntityInfo) {
      mPageAdapter.updatePage(DETAILS_CONTAINER_ID,
          getMapEntityDetailsContainerTitle(kmlEntityInfo.getName()),
          new MapEntityDetailsFragmentFactory());
    }

    private void addDetailsPage(KmlEntityInfo kmlEntityInfo) {
      mPageAdapter.addPage(DETAILS_CONTAINER_ID,
          getMapEntityDetailsContainerTitle(kmlEntityInfo.getName()),
          new MapEntityDetailsFragmentFactory());
    }
  }
}

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
import com.teamagam.gimelgimel.app.dynamic_layer.DynamicLayerDetailsFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityPanel;
import com.teamagam.gimelgimel.app.map.details.MapEntityDetailsFragment;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.domain.dynamicLayers.details.ClearDisplayedDynamicLayerDetailsInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.details.DisplaySelectedDynamicLayerDetailsInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.details.DisplaySelectedDynamicLayerDetailsInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.details.DynamicLayerClickInfo;
import com.teamagam.gimelgimel.domain.map.DisplayKmlEntityInfoInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayKmlEntityInfoInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectKmlEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;

@AutoFactory
public class PanelViewModel extends BaseViewModel<MainActivityPanel> {

  private static final int MESSAGES_CONTAINER_ID = 0;
  private static final int DETAILS_CONTAINER_ID = 1;
  private static final int DYNAMIC_LAYER_CONTAINER_ID = 2;

  private final Context mContext;
  private final DisplaySelectedDynamicLayerDetailsInteractorFactory
      mDisplaySelectedDynamicLayerDetailsInteractorFactory;
  private final ClearDisplayedDynamicLayerDetailsInteractor
      mClearDisplayedDynamicLayerDetailsInteractor;
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
  private DisplaySelectedDynamicLayerDetailsInteractor
      mDisplaySelectedDynamicLayerDetailsInteractor;

  PanelViewModel(@Provided Context context,
      @Provided SelectKmlEntityInteractorFactory selectKmlEntityInteractorFactory,
      @Provided
          DisplayUnreadMessagesCountInteractorFactory displayUnreadMessagesCountInteractorFactory,
      @Provided DisplaySelectedMessageInteractorFactory displaySelectedMessageInteractorFactory,
      @Provided DisplayKmlEntityInfoInteractorFactory displayKmlEntityInfoInteractorFactory,
      @Provided
          DisplaySelectedDynamicLayerDetailsInteractorFactory displaySelectedDynamicLayerDetailsInteractorFactory,
      @Provided
          ClearDisplayedDynamicLayerDetailsInteractor clearDisplayedDynamicLayerDetailsInteractor,
      FragmentManager fragmentManager) {
    mContext = context;
    mSelectKmlEntityInteractorFactory = selectKmlEntityInteractorFactory;
    mDisplayUnreadCountInteractorFactory = displayUnreadMessagesCountInteractorFactory;
    mDisplaySelectedMessageInteractorFactory = displaySelectedMessageInteractorFactory;
    mDisplayKmlEntityInfoInteractorFactory = displayKmlEntityInfoInteractorFactory;
    mDisplaySelectedDynamicLayerDetailsInteractorFactory =
        displaySelectedDynamicLayerDetailsInteractorFactory;
    mClearDisplayedDynamicLayerDetailsInteractor = clearDisplayedDynamicLayerDetailsInteractor;
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
    updateCurrentlySelectedPageId();
    if (mCurrentlySelectedPageId == MESSAGES_CONTAINER_ID) {
      resetPages();
      clearSelections();
    }
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
    mDisplaySelectedDynamicLayerDetailsInteractor =
        mDisplaySelectedDynamicLayerDetailsInteractorFactory.create(
            new DynamicLayerDetailsDisplayer());
  }

  private void executeDisplayInteractors() {
    execute(mDisplayUnreadMessagesCountInteractor, mDisplaySelectedMessageInteractor,
        mDisplayKmlEntityInfoInteractor, mDisplaySelectedDynamicLayerDetailsInteractor);
  }

  private void unsubscribeDisplayInteractors() {
    unsubscribe(mDisplayUnreadMessagesCountInteractor, mDisplaySelectedMessageInteractor,
        mDisplayKmlEntityInfoInteractor, mDisplaySelectedDynamicLayerDetailsInteractor);
  }

  private void updateCurrentlySelectedPageId() {
    mCurrentlySelectedPageId = mPageAdapter.getId(mView.getCurrentPagePosition());
  }

  private void resetPages() {
    mPageAdapter.removePage(DYNAMIC_LAYER_CONTAINER_ID);
    mPageAdapter.removePage(DETAILS_CONTAINER_ID);
  }

  private void clearSelections() {
    mClearDisplayedDynamicLayerDetailsInteractor.execute();
    mSelectKmlEntityInteractorFactory.create(null).execute();
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

  private class SelectedMessageDisplayer implements DisplaySelectedMessageInteractor.Displayer {

    @Override
    public void display(ChatMessage message) {
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

  private abstract class PageDisplayer<T> {

    private final int mPageId;

    public PageDisplayer(int pageId) {
      mPageId = pageId;
    }

    void display(T data) {
      resetPages();
      createPage(data);
      setAsCurrentPage();
      updateCurrentlySelectedPageId();
      mView.anchorSlidingPanel();
    }

    void hide() {
      mPageAdapter.removePage(mPageId);
      updateCurrentlySelectedPageId();
    }

    protected abstract String getTitle(T data);

    protected abstract BottomPanelPagerAdapter.FragmentFactory getFragmentFactory(T data);

    private void createPage(T data) {
      mPageAdapter.addPage(mPageId, getTitle(data), getFragmentFactory(data));
    }

    private void setAsCurrentPage() {
      mView.setCurrentPage(mPageAdapter.getPosition(mPageId));
    }
  }

  private class KmlEntityInfoDisplayer extends PageDisplayer<KmlEntityInfo>
      implements DisplayKmlEntityInfoInteractor.Displayer {

    public KmlEntityInfoDisplayer() {
      super(DETAILS_CONTAINER_ID);
    }

    @Override
    public void display(KmlEntityInfo kmlEntityInfo) {
      super.display(kmlEntityInfo);
    }

    @Override
    public void hide() {
    }

    @Override
    protected String getTitle(KmlEntityInfo data) {
      return getMapEntityDetailsContainerTitle(data.getName());
    }

    @Override
    protected BottomPanelPagerAdapter.FragmentFactory getFragmentFactory(KmlEntityInfo data) {
      return MapEntityDetailsFragment::new;
    }
  }

  private class DynamicLayerDetailsDisplayer extends PageDisplayer<DynamicLayerClickInfo>
      implements DisplaySelectedDynamicLayerDetailsInteractor.Displayer {
    public DynamicLayerDetailsDisplayer() {
      super(DYNAMIC_LAYER_CONTAINER_ID);
    }

    @Override
    public void display(DynamicLayerClickInfo dynamicLayer) {
      super.display(dynamicLayer);
    }

    @Override
    protected String getTitle(DynamicLayerClickInfo data) {
      return data.getDynamicLayer().getName();
    }

    @Override
    protected BottomPanelPagerAdapter.FragmentFactory getFragmentFactory(DynamicLayerClickInfo data) {
      return () -> DynamicLayerDetailsFragment.newInstance(data.getDynamicLayer().getId(),
          data.getDynamicEntity().getId());
    }
  }
}

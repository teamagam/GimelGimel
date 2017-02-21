package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.base.adapters.DynamicBottomPanelPagerAdapter;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityPanel;
import com.teamagam.gimelgimel.app.message.view.MessagesContainerFragment;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesContainerStateInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractor;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesContainerStateRepository;

import javax.inject.Inject;

@AutoFactory
public class PanelViewModel extends BaseViewModel<MainActivityPanel> {

    private static final int MESSAGES_CONTAINER_POSITION = 0;
    private static final int SENSORS_CONTAINER_POSITION = 1;
    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private final UpdateMessagesReadInteractorFactory mUpdateMessagesReadInteractorFactory;
    private final UpdateMessagesContainerStateInteractorFactory mUpdateMessagesContainerStateInteractorFactory;
    private final DisplayUnreadMessagesCountInteractorFactory mDisplayUnreadCountInteractorFactory;
    private final DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
    protected AppLogger sLogger = AppLoggerFactory.create();
    private UpdateMessagesReadInteractor mMessagesReadInteractor;
    private DisplayUnreadMessagesCountInteractor mDisplayUnreadMessagesCountInteractor;
    private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;
    private DynamicBottomPanelPagerAdapter mPageAdapter;
    private int mLastPagePosition;

    @Inject
    PanelViewModel(@Provided Context context,
                   @Provided UpdateMessagesReadInteractorFactory
                           updateMessagesReadInteractorFactory,
                   @Provided UpdateMessagesContainerStateInteractorFactory
                           updateMessagesContainerStateInteractorFactory,
                   @Provided DisplayUnreadMessagesCountInteractorFactory
                           displayUnreadMessagesCountInteractorFactory,
                   @Provided DisplaySelectedMessageInteractorFactory
                           displaySelectedMessageInteractorFactory,
                   FragmentManager fragmentManager) {
        mContext = context;
        mUpdateMessagesReadInteractorFactory = updateMessagesReadInteractorFactory;
        mUpdateMessagesContainerStateInteractorFactory = updateMessagesContainerStateInteractorFactory;
        mDisplayUnreadCountInteractorFactory = displayUnreadMessagesCountInteractorFactory;
        mDisplaySelectedMessageInteractorFactory = displaySelectedMessageInteractorFactory;
        mFragmentManager = fragmentManager;
    }

    public static boolean isClosedState(SlidingUpPanelLayout.PanelState state) {
        return state == SlidingUpPanelLayout.PanelState.COLLAPSED
                || state == SlidingUpPanelLayout.PanelState.HIDDEN;
    }

    public static boolean isOpenState(SlidingUpPanelLayout.PanelState state) {
        return state == SlidingUpPanelLayout.PanelState.ANCHORED
                || state == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

    @Override
    public void start() {
        super.start();
        mPageAdapter = new DynamicBottomPanelPagerAdapter(mFragmentManager);
        setInitialPages();
        mView.setAdapter(mPageAdapter);
        mMessagesReadInteractor = mUpdateMessagesReadInteractorFactory.create();
        mDisplayUnreadMessagesCountInteractor = mDisplayUnreadCountInteractorFactory.create(
                new DisplayUnreadMessagesCountInteractor.Renderer() {
                    @Override
                    public void renderUnreadMessagesCount(int unreadMessagesCount) {
                        mPageAdapter.updateTitle(MESSAGES_CONTAINER_POSITION,
                                getMessagesContainerTitle(unreadMessagesCount));
                    }
                }
        );
        mDisplaySelectedMessageInteractor = mDisplaySelectedMessageInteractorFactory.create(
                new SelectedMessageDisplayer());

        mDisplayUnreadMessagesCountInteractor.execute();
        mDisplaySelectedMessageInteractor.execute();
    }

    private void setInitialPages() {
        mPageAdapter.addPage(getMessagesContainerTitle(0), new DynamicBottomPanelPagerAdapter.FragmentFactory() {
            @Override
            public Fragment create() {
                return new MessagesContainerFragment();
            }
        }, MESSAGES_CONTAINER_POSITION);
        mLastPagePosition = MESSAGES_CONTAINER_POSITION;
    }

    @Override
    public void stop() {
        super.stop();
        mMessagesReadInteractor.unsubscribe();
        mDisplayUnreadMessagesCountInteractor.unsubscribe();
        mDisplaySelectedMessageInteractor.unsubscribe();
    }

    public void onPageSelected(int position) {
        if (mView.isSlidingPanelOpen()) {
            onPageSelectedWithOpenPanel(position);
        }
        mLastPagePosition = position;
    }

    public void onChangePanelState(SlidingUpPanelLayout.PanelState newState) {
        if (mView.isMessagesContainerSelected()) {
            onChangePanelStateWithMessagesSelected(newState);
        }
    }

    private void onPageSelectedWithOpenPanel(int position) {
        if (isMessagesPage(position)) {
            onMessagesContainerRevealed();
        } else if (isMessagesPage(mLastPagePosition)){
            onMessagesContainerConcealed();
        }
    }

    public boolean isMessagesPage(int position) {
        return position == MESSAGES_CONTAINER_POSITION;
    }

    private void onChangePanelStateWithMessagesSelected(SlidingUpPanelLayout.PanelState newState) {
        if (isClosedState(newState)) {
            onMessagesContainerConcealed();
        } else if (isOpenState(newState)) {
            onMessagesContainerRevealed();
        }
    }

    private void onMessagesContainerRevealed() {
        mMessagesReadInteractor.execute();
        mUpdateMessagesContainerStateInteractorFactory.create(
                MessagesContainerStateRepository.ContainerState.VISIBLE).execute();

    }

    private void onMessagesContainerConcealed() {
        mMessagesReadInteractor.execute();
        mUpdateMessagesContainerStateInteractorFactory.create(
                MessagesContainerStateRepository.ContainerState.INVISIBLE).execute();
    }

    private String getMessagesContainerTitle(int unreadMessagesCount) {
        return getMessagesTitle() + getMessagesCounterExtension(unreadMessagesCount);
    }

    private String getMessagesTitle() {
        return mContext.getString(R.string.messages_container_title);
    }

    private String getMessagesCounterExtension(int unreadMessagesCount) {
        if (unreadMessagesCount > 0) {
            return mContext.getString(R.string.bottom_panel_messages_counter,
                    unreadMessagesCount);
        } else {
            return "";
        }
    }

    private String getSensorsContainerTitle() {
        return mContext.getString(R.string.sensors_container_title);
    }

    private class SelectedMessageDisplayer implements DisplaySelectedMessageInteractor.Displayer {
        @Override
        public void display(Message message) {
            mView.changePanelPage(0);
            mView.anchorSlidingPanel();
        }
    }
}

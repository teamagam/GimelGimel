package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.base.adapters.BottomPanelPagerAdapter;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityPanel;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractor;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractorFactory;

import javax.inject.Inject;

public class PanelViewModel extends BaseViewModel<MainActivityPanel> {

    protected AppLogger sLogger = AppLoggerFactory.create();
    @Inject
    UpdateMessagesReadInteractorFactory mMessagesReadInteractorFactory;
    @Inject
    DisplayUnreadMessagesCountInteractorFactory mDisplayUnreadCountInteractorFactory;
    private UpdateMessagesReadInteractor mMessagesReadInteractor;
    private DisplayUnreadMessagesCountInteractor mDisplayUnreadMessagesCountInteractor;

    @Inject
    PanelViewModel() {
    }

    @Override
    public void start() {
        super.start();
        mMessagesReadInteractor = mMessagesReadInteractorFactory.create();
        mDisplayUnreadMessagesCountInteractor = mDisplayUnreadCountInteractorFactory.create(
                new DisplayUnreadMessagesCountInteractor.Renderer() {
                    @Override
                    public void renderUnreadMessagesCount(int unreadMessagesCount) {
                        mView.updateUnreadCount(unreadMessagesCount);
                    }
                }
        );
        mDisplayUnreadMessagesCountInteractor.execute();
    }

    @Override
    public void stop() {
        super.stop();
        mDisplayUnreadMessagesCountInteractor.unsubscribe();
        mMessagesReadInteractor.unsubscribe();
    }

    public void onPageSelected(int position) {
        switch (position) {
            case BottomPanelPagerAdapter.SENSORS_CONTAINER_POSITION:
                onHideMessagesContainer();
                break;
            case BottomPanelPagerAdapter.MESSAGES_CONTAINER_POSITION:
                onShowMessagesContainer();
                break;
            default:
        }
    }

    public void onChangePanelState(SlidingUpPanelLayout.PanelState newState) {
        if(isClosed(newState)) {
            onHideMessagesContainer();
        } else {
            onShowMessagesContainer();
        }
    }

    private void onShowMessagesContainer() {
        mMessagesReadInteractor.execute();
        mView.updateUnreadCount(0);
    }

    private void onHideMessagesContainer() {
        mMessagesReadInteractorFactory.create().execute();
    }

    private boolean isClosed(SlidingUpPanelLayout.PanelState state) {
        return state == SlidingUpPanelLayout.PanelState.COLLAPSED
                || state == SlidingUpPanelLayout.PanelState.HIDDEN;
    }

}

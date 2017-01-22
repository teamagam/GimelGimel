package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityPanel;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplayUnreadMessagesCountInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesContainerStateInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractor;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesContainerStateRepository;

import javax.inject.Inject;

public class PanelViewModel extends BaseViewModel<MainActivityPanel> {

    protected AppLogger sLogger = AppLoggerFactory.create();
    @Inject
    UpdateMessagesReadInteractorFactory mMessagesReadInteractorFactory;
    @Inject
    UpdateMessagesContainerStateInteractorFactory mUpdateMessagesContainerStateInteractorFactory;
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
        mUpdateMessagesContainerStateInteractorFactory.create(MessagesContainerStateRepository.ContainerState.INVISIBLE).execute();
        mDisplayUnreadMessagesCountInteractor.execute();
    }

    @Override
    public void stop() {
        super.stop();
        mMessagesReadInteractor.unsubscribe();
        mDisplayUnreadMessagesCountInteractor.unsubscribe();
    }

    public void onPageSelected(int position) {
        if (mView.isSlidingPanelOpen()) {
            if (mView.isSensorsPage(position)) {
                onMessagesContainerConcealed();
            } else if (mView.isMessagesPage(position)) {
                onMessagesContainerRevealed();
            }
        }
    }

    public void onChangePanelState(SlidingUpPanelLayout.PanelState newState) {
        if (mView.isMessagesContainerSelected()) {
            if (mView.isClosedState(newState)) {
                onMessagesContainerConcealed();
            } else if (mView.isOpenState(newState)) {
                onMessagesContainerRevealed();
            }
        }
    }

    private void onMessagesContainerRevealed() {
        mMessagesReadInteractor.execute();
        mUpdateMessagesContainerStateInteractorFactory.create(MessagesContainerStateRepository.ContainerState.VISIBLE).execute();
    }

    private void onMessagesContainerConcealed() {
        mMessagesReadInteractor.execute();
        mUpdateMessagesContainerStateInteractorFactory.create(MessagesContainerStateRepository.ContainerState.INVISIBLE).execute();
    }
}

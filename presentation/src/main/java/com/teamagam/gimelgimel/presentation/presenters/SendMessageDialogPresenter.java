package com.teamagam.gimelgimel.presentation.presenters;

import presenters.base.BasePresenter;
import ui.base.BaseView;

public interface SendMessageDialogPresenter extends BasePresenter {
    interface View extends BaseView {
        void displayMessageStatus();
    }
}

package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.presentation.presenters.base.BasePresenter;
import com.teamagam.gimelgimel.presentation.ui.base.BaseView;

public interface SendMessagePresenter extends BasePresenter {
    void sendMessage(String userMessage, View completed);

    interface View extends BaseView {
        void displayMessageStatus();
    }
}

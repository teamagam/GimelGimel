package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.message.view.SendImageFragment;
import com.teamagam.gimelgimel.domain.messages.SendImageMessageInteractorFactory;

import javax.inject.Inject;

public class SendImageMessageViewModel {

    @Inject
    SendImageMessageInteractorFactory mInteractorFactory;

    private SendImageFragment mSendImageFragment;

    @Inject
    public SendImageMessageViewModel() {

    }

    public void setView(SendImageFragment sendImageFragment) {
        mSendImageFragment = sendImageFragment;
    }

    public void onFabClicked() {
        mSendImageFragment.takePicture();
    }

    public void sendImage(String imagePath) {
        long imageTime = System.currentTimeMillis();
        mInteractorFactory.create(imageTime, imagePath).execute();
    }
}

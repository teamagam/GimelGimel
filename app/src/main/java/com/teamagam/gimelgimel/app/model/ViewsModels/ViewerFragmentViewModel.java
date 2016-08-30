package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

import javax.inject.Inject;

public class ViewerFragmentViewModel {

    @Inject SendImageMessageInteractor mInteractor;
    SendImageMessagePresenter mPresenter;

    // TODO: We have to remove the Presenter classes, it's definitely un-necessary
    public ViewerFragmentViewModel(SendImageMessagePresenter.View view) {
        mPresenter = new SendImageMessagePresenter(mInteractor, view);
    }

    public void sendImage(String senderId, String imageUrl, double latitude, double longitude) {
        PointGeometry location = new PointGeometry(latitude, longitude);

        mPresenter.sendImageMessage(senderId, imageUrl, location);
    }

    public void sendImage(String senderId, String imageUrl) {
        mPresenter.sendImageMessage(senderId, imageUrl, null);
    }
}

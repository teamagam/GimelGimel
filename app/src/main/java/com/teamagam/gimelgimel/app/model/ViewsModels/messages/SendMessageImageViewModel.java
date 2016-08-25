package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

public class SendMessageImageViewModel {

    SendImageMessagePresenter.View mView;
    SendImageMessageInteractor mInteractor;
    ImagesRepository mImagesRepository;

    public SendMessageImageViewModel(SendImageMessagePresenter.View view, ImagesRepository repository) {
        mView = view;
        mImagesRepository = repository;

        mInteractor = new SendImageMessageInteractor(null, null, mImagesRepository);
    }
}

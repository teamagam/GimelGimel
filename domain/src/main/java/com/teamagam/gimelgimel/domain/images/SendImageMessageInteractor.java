package com.teamagam.gimelgimel.domain.images;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;

public class SendImageMessageInteractor extends AbstractInteractor {

    private ImagesRepository mImagesRepository;
    private MessagesRepository mMessagesRepository;
    private MessageImage mMessageImage;
    private String mImagePath;

    public SendImageMessageInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                      ImagesRepository repository, MessagesRepository messagesRepository) {
        super(threadExecutor, postExecutionThread);

        mImagesRepository = repository;
        mMessagesRepository = messagesRepository;
    }

    public void sendImageMessage(Subscriber subscriber, MessageImage messageImage, String imagePath) {
        if (isValid(subscriber, messageImage, imagePath)) {
            mMessageImage = messageImage;
            mImagePath = imagePath;

            execute(subscriber);
        } else {
            throw new IllegalArgumentException("None of the arguments can be null");
        }
    }

    private boolean isValid(Subscriber subscriber, MessageImage messageImage, String imagePath) {
        return !(subscriber == null || messageImage == null || imagePath.isEmpty());
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mImagesRepository.uploadImage(mMessageImage, mImagePath)
                .doOnCompleted(() -> mMessagesRepository.putMessage(mMessageImage));
    }
}

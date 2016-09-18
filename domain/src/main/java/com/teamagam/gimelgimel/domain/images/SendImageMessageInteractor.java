package com.teamagam.gimelgimel.domain.images;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;
import rx.Subscriber;

public class SendImageMessageInteractor extends AbstractInteractor {

    private static final String IMAGE_SOURCE = "User";

    private ImagesRepository mImagesRepository;
    private MessagesRepository mMessagesRepository;
    private UserPreferencesRepository mUserPreferencesRepository;
    private LocationRepository mLocationRepository;
    private String mImagePath;
    private long mTime;

    public SendImageMessageInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                      ImagesRepository repository, MessagesRepository messagesRepository,
                                      UserPreferencesRepository userPreferencesRepository,
                                      LocationRepository locationRepository) {
        super(threadExecutor, postExecutionThread);

        mImagesRepository = repository;
        mMessagesRepository = messagesRepository;
        mUserPreferencesRepository = userPreferencesRepository;
        mLocationRepository = locationRepository;
    }

    public void sendImageMessage(Subscriber subscriber, String imagePath, long time) {
        if (!isValid(subscriber, imagePath)) {
            throw new IllegalArgumentException("None of the arguments can be null");
        }

        mImagePath = imagePath;
        mTime = time;

        execute(subscriber);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<String> senderId = mUserPreferencesRepository.getSenderId();
        Observable<PointGeometry> location = mLocationRepository.getLocation();

        return Observable
                .combineLatest(senderId, location, (sender, point) ->
                        new MessageImage(sender, new ImageMetadata(mTime, mImagePath, point, IMAGE_SOURCE)))
                .flatMap(message -> mImagesRepository.uploadImage(message, mImagePath))
                .doOnNext(messageImage -> mMessagesRepository.putMessage(messageImage));
    }

    private boolean isValid(Subscriber subscriber, String imagePath) {
        return !(subscriber == null || imagePath == null || imagePath.isEmpty());
    }
}

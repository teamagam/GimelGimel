package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;

import rx.Observable;

public interface ImagesRepository {
    Observable<Message> uploadImage(MessageImage message);

    Observable<String> createImageTempPath();

    String getImagePath();
}

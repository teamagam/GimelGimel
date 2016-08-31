package com.teamagam.gimelgimel.domain.images.repository;

import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;

import rx.Observable;

public interface ImagesRepository {
    Observable<MessageImage> uploadImage(MessageImage message, String filePath);

    Observable<byte[]> getImageBytes(String imagePath);
}

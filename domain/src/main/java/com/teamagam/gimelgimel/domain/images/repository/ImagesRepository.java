package com.teamagam.gimelgimel.domain.images.repository;

import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;

public interface ImagesRepository {
    void uploadImage(MessageImage message, String filePath);

    byte[] getImageBytes(String imagePath);
}

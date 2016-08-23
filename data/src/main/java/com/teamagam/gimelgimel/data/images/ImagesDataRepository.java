package com.teamagam.gimelgimel.data.images;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;

import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImagesDataRepository implements ImagesRepository {

    private GGMessagingAPI mApi;
    private MessageDataMapper mMessageMapper;

    @Inject
    public ImagesDataRepository(GGMessagingAPI api, MessageDataMapper mapper) {
        mApi = api;
        mMessageMapper = mapper;
    }

    @Override
    public void uploadImage(MessageImage message, byte[] imageBytes) {
        MessageData messageData = mMessageMapper.transformToData(message);
        MultipartBody.Part body = createMultipartBody(imageBytes);

        mApi.sendImage(messageData, body);
    }

    private MultipartBody.Part createMultipartBody(byte[] imageBytes) {
        MediaType mimeType = MediaType.parse(Constants.IMAGE_MIME_TYPE);
        RequestBody requestFile =
                RequestBody.create(mimeType, imageBytes, 0, imageBytes.length);
        String randomFileName = UUID.randomUUID().toString();

        return MultipartBody.Part.createFormData(Constants.IMAGE_KEY, randomFileName, requestFile);
    }
}

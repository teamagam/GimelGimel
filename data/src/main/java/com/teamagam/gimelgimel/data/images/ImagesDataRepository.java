package com.teamagam.gimelgimel.data.images;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class ImagesDataRepository implements ImagesRepository {

    private GGMessagingAPI mApi;
    private MessageDataMapper mMessageMapper;

    @Inject
    public ImagesDataRepository(GGMessagingAPI api, MessageDataMapper mapper) {
        mApi = api;
        mMessageMapper = mapper;
    }

    @Override
    public Observable<MessageImage> uploadImage(MessageImage message, String filePath) {
        MessageData messageData = mMessageMapper.transformToData(message);
        File imageFile = new File(filePath);
        byte[] compressedImageBytes = ImageUtils.readAndCompressImage(imageFile);
        MultipartBody.Part body = createMultipartBody(imageFile.getName(), compressedImageBytes);

        return mApi.sendImage(messageData, body)
                .map(returnedMessage ->
                        (MessageImage) mMessageMapper.transform(returnedMessage));
    }

    @Override
    public Observable<byte[]> getImageBytes(String imagePath) {
        return Observable.just(imagePath).map(path -> {
            File image = new File(imagePath);
            int imageFileSize = (int) image.length();
            byte[] imageBytes = new byte[imageFileSize];

            try {
                FileInputStream inputStream = new FileInputStream(image);
                inputStream.read(imageBytes, 0, imageBytes.length);
                inputStream.close();

                return imageBytes;
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        });
    }

    private MultipartBody.Part createMultipartBody(String fileName, byte[] imageBytes) {
        MediaType mimeType = MediaType.parse(Constants.IMAGE_MIME_TYPE);
        RequestBody requestFile = RequestBody.create(mimeType, imageBytes);

        return MultipartBody.Part.createFormData(Constants.IMAGE_KEY, fileName, requestFile);
    }
}

package com.teamagam.gimelgimel.data.images;

import android.content.Context;
import android.net.Uri;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.repository.ImagesRepository;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

@Singleton
public class ImagesDataRepository implements ImagesRepository {

    private Context mContext;
    private GGMessagingAPI mApi;
    private MessageDataMapper mMessageMapper;
    private Uri mImageTempUri;

    @Inject
    public ImagesDataRepository(Context context, GGMessagingAPI api, MessageDataMapper mapper) {
        mContext = context;
        mApi = api;
        mMessageMapper = mapper;
    }

    @Override
    public Observable<Message> uploadImage(MessageImage message) {
        MessageData messageData = mMessageMapper.transformToData(message);
        File imageFile = new File(message.getImageMetadata().getURL());
        byte[] compressedImageBytes = ImageUtils.readAndCompressImage(imageFile);
        MultipartBody.Part body = createMultipartBody(imageFile.getName(), compressedImageBytes);

        return mApi.sendImage(messageData, body)
                .map(mMessageMapper::transform);
    }

    @Override
    public Observable<String> createImageTempPath() {
        return Observable.just(mContext)
                .map(ImageUtils::getTempImageUri)
                .doOnNext(this::storeUri)
                .map(Uri::toString);
    }

    public String getImagePath(){
        return mImageTempUri.getPath();
    }

    private void storeUri(Uri uri) {
        mImageTempUri = uri;
    }

    private MultipartBody.Part createMultipartBody(String fileName, byte[] imageBytes) {
        MediaType mimeType = MediaType.parse(Constants.IMAGE_MIME_TYPE);
        RequestBody requestFile = RequestBody.create(mimeType, imageBytes);

        return MultipartBody.Part.createFormData(Constants.IMAGE_KEY, fileName, requestFile);
    }
}

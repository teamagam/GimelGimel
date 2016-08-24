package com.teamagam.gimelgimel.data.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

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
    public void uploadImage(MessageImage message, String imagePath) {
        MessageData messageData = mMessageMapper.transformToData(message);
        File image = new File(imagePath);
        byte[] compressedImage = compressImage(image);
        MultipartBody.Part body = createMultipartBody(image.getName(), compressedImage);

        mApi.sendImage(messageData, body);
    }

    private MultipartBody.Part createMultipartBody(String fileName, byte[] imageBytes) {
        MediaType mimeType = MediaType.parse(Constants.IMAGE_MIME_TYPE);
        RequestBody requestFile = RequestBody.create(mimeType, imageBytes);

        return MultipartBody.Part.createFormData(Constants.IMAGE_KEY, fileName, requestFile);
    }

    private byte[] compressImage(File image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), options);

        int scale = 1;
        if (options.outHeight > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS || options.outWidth > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS) {
            scale = (int) Math.pow(2,
                    (int) Math.ceil(Math.log(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS /
                            (double) Math.max(options.outHeight, options.outWidth)) / Math.log(
                            0.5)));
        }

        //Decode with inSampleSize
        options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap compressedBitmap = BitmapFactory.decodeFile(image.getPath(), options);

        compressedBitmap.compress(Constants.IMAGE_COMPRESS_TYPE, Constants.COMPRESSED_IMAGE_JPEG_QUALITY, stream);

        return stream.toByteArray();
    }
}

package com.teamagam.gimelgimel.data.response.repository.cloud;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.images.ImageUtils;
import com.teamagam.gimelgimel.data.response.entity.ConfirmMessageReadResponse;
import com.teamagam.gimelgimel.data.response.entity.GGResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageResponse;
import com.teamagam.gimelgimel.data.response.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

@Singleton
public class CloudMessagesSource {

  @Inject
  GGMessagingAPI mMessagingApi;
  @Inject
  ImageUtils mImageUtils;

  @Inject
  CloudMessagesSource() {
  }

  public Observable<GGResponse> sendMessage(final GGResponse message) {
    if (isImageMessage(message)) {
      return sendImage((ImageResponse) message);
    } else {
      return mMessagingApi.postMessage(message);
    }
  }

  public void informReadMessage(ConfirmMessageReadResponse confirmRead) {
    mMessagingApi.informReadMessage(confirmRead).subscribe(new SimpleSubscriber<>());
  }

  private Observable<GGResponse> sendImage(ImageResponse imageMessage) {
    if (Constants.SHOULD_USE_BASE64_IMAGE) {
      imageMessage.getContent().setBase64(getBase64Image(imageMessage));
      return mMessagingApi.sendImage(imageMessage);
    } else {
      MultipartBody.Part imageFileParts = getImageFilePart(imageMessage);
      return mMessagingApi.sendImage(imageMessage, imageFileParts);
    }
  }

  public Observable<List<GGResponse>> getMessages() {
    return mMessagingApi.getMessages();
  }

  private boolean isImageMessage(GGResponse message) {
    return message.getType().equals(GGResponse.IMAGE);
  }

  private MultipartBody.Part getImageFilePart(ImageResponse message) {
    File imageFile = new File(message.getContent().getLocalUrl());
    byte[] compressedImageBytes = mImageUtils.readAndCompressImage(imageFile);
    return createMultipartBody(imageFile.getName(), compressedImageBytes);
  }

  private MultipartBody.Part createMultipartBody(String fileName, byte[] imageBytes) {
    MediaType mimeType = MediaType.parse(Constants.IMAGE_MIME_TYPE);
    RequestBody requestFile = RequestBody.create(mimeType, imageBytes);

    return MultipartBody.Part.createFormData(Constants.IMAGE_KEY, fileName, requestFile);
  }

  private String getBase64Image(ImageResponse message) {
    File imageFile = new File(message.getContent().getLocalUrl());

    return mImageUtils.convertImageToBase64(imageFile);
  }
}

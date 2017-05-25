package com.teamagam.gimelgimel.data.message.repository.cloud;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.images.ImageUtils;
import com.teamagam.gimelgimel.data.message.entity.ConfirmMessageReadData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
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

  public Observable<MessageData> sendMessage(final MessageData message) {
    if (isImageMessage(message)) {
      return sendImage((MessageImageData) message);
    } else {
      return mMessagingApi.postMessage(message);
    }
  }

  public void informReadMessage(ConfirmMessageReadData confirmRead) {
    mMessagingApi.informReadMessage(confirmRead).subscribe(new SimpleSubscriber<>());
  }

  private Observable<MessageData> sendImage(MessageImageData imageMessage) {
    if (Constants.SHOULD_USE_BASE64_IMAGE) {
      imageMessage.getContent().setBase64(getBase64Image(imageMessage));
      return mMessagingApi.sendImage(imageMessage);
    } else {
      MultipartBody.Part imageFileParts = getImageFilePart(imageMessage);
      return mMessagingApi.sendImage(imageMessage, imageFileParts);
    }
  }

  public Observable<List<MessageData>> getMessages() {
    return mMessagingApi.getMessages();
  }

  private boolean isImageMessage(MessageData message) {
    return message.getType().equals(MessageData.IMAGE);
  }

  private MultipartBody.Part getImageFilePart(MessageImageData message) {
    File imageFile = new File(message.getContent().getLocalUrl());
    byte[] compressedImageBytes = mImageUtils.readAndCompressImage(imageFile);
    return createMultipartBody(imageFile.getName(), compressedImageBytes);
  }

  private MultipartBody.Part createMultipartBody(String fileName, byte[] imageBytes) {
    MediaType mimeType = MediaType.parse(Constants.IMAGE_MIME_TYPE);
    RequestBody requestFile = RequestBody.create(mimeType, imageBytes);

    return MultipartBody.Part.createFormData(Constants.IMAGE_KEY, fileName, requestFile);
  }

  private String getBase64Image(MessageImageData message) {
    File imageFile = new File(message.getContent().getLocalUrl());

    return mImageUtils.convertImageToBase64(imageFile);
  }
}

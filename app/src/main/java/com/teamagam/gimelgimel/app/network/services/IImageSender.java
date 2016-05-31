package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.net.Uri;

/**
 * Created on 5/22/2016.
 * Interface for uploading an image file with metadata
 */
public interface IImageSender {

    String ACTION_IMAGE_SENDING =
            "com.teamagam.gimelgimel.app.network.services.IMAGE_SENDING";

    String TIME = "time";
    String LOCATION = "location";

    void sendImage(Context context, Uri mImageUri);




}

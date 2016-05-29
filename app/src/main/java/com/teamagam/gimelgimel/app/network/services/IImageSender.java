package com.teamagam.gimelgimel.app.network.services;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Created on 5/22/2016.
 * Interface for uploading an image file with metadata
 */
public interface IImageSender{
    void sendImage(Uri imageUri, @Nullable PointGeometry location, long imageTime);
}

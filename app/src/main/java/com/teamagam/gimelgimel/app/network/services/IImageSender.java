package com.teamagam.gimelgimel.app.network.services;

import android.net.Uri;

import com.teamagam.gimelgimel.app.model.entities.LocationSample;

/**
 * Created on 5/22/2016.
 * Interface for sending and uploading images
 */
public interface IImageSender{
    void sendImage(Uri imageUri, LocationSample imageLocation, long imageTime);
}

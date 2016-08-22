package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * Created on 5/31/2016.
 * Image sender, creates service for pre-processing and uploading image.
 */
public class GGImageSender implements IImageSender {

    @Override
    public void sendImage(Context context, Uri mImageUri, long imageTime, PointGeometry loc) {

        Intent intent = new Intent(context, GGImageService.class);
        intent.setAction(IImageSender.ACTION_IMAGE_SENDING);
        intent.setData(mImageUri);
        intent.putExtra(IImageSender.TIME, imageTime);
        intent.putExtra(IImageSender.LOCATION, loc);
        context.startService(intent);
    }

}

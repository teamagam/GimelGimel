package com.teamagam.gimelgimel.app.network.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Date;

/**
 * Created on 5/31/2016.
 * Image sender, calls service for pre-processing and uploading image.
 */
public class GGImageSender implements IImageSender {

    @Override
    public void sendImage(Context context, Uri mImageUri) {

        //todo: q: where do we want to take all that data? here?
        LocationSample imageLocation = LocationFetcher.getInstance(context).getLastKnownLocation();
        long imageTime = new Date().getTime();
        PointGeometry loc = null;
        if (imageLocation != null) {
            loc = imageLocation.getLocation();
        }

        Intent intent = new Intent(context, GGImageService.class);
        intent.setAction(IImageSender.ACTION_IMAGE_SENDING);
        intent.setData(mImageUri);
        intent.putExtra(IImageSender.TIME, imageTime);
        intent.putExtra(IImageSender.LOCATION, loc);
        context.startService(intent);
    }

}

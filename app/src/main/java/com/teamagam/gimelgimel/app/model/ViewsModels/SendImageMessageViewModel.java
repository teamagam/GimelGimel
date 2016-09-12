package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.utils.ImageUtil;
import com.teamagam.gimelgimel.app.view.fragments.SendImageFragment;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

import java.io.IOException;

import javax.inject.Inject;

import rx.exceptions.Exceptions;

public class SendImageMessageViewModel implements SendImageMessagePresenter.View {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String IMAGE_SOURCE = "User";

    @Inject
    SendImageMessageInteractor mInteractor;

    private Uri mImageUri;
    private LocationFetcher mLocationFetcher;
    private SendImageFragment mSendImageFragment;
    private GGApplication mApp;

    public SendImageMessageViewModel(SendImageFragment sendImageFragment) {
        mSendImageFragment = sendImageFragment;
        mApp = (GGApplication) sendImageFragment.getActivity().getApplicationContext();
        mLocationFetcher = LocationFetcher.getInstance(mApp);

        mApp.getMessagesComponent().inject(this);
    }

    @Override
    public void displayMessageStatus() {
        Toast.makeText(mSendImageFragment.getActivity(), "The image has been sent", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showProgress() {
        Toast.makeText(mSendImageFragment.getActivity(), "Sending image", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showError(String message) {
        //sLogger.e(message);
        Toast.makeText(mSendImageFragment.getActivity(), "An error has occurred while sending image: " + message, Toast.LENGTH_LONG)
                .show();
    }

    public void onFabClick(View view) {
        takePicture();
    }

    public void sendImage() {
        showProgress();

        String imagePath = mImageUri.getPath();
        String senderId = mApp.getPrefs().getString(R.string.user_name_text_key);
        LocationSample locationSample = getLocationSample();
        PointGeometry location = createPointGeometry(locationSample);
        SendImageMessagePresenter presenter = createPresenter();

        mInteractor.sendImageMessage(presenter, createMessageImage(senderId, location, imagePath), imagePath);
    }

    private void takePicture() {
        try {
            //sLogger.userInteraction("Start camera activity button clicked");

            mImageUri = createImageUri();
            startCameraIntent(mImageUri);
        } catch (Exception e) {
            Toast.makeText(mApp, "Problem with taking images - Couldn't create temp file", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCameraIntent(Uri mImageUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        if (takePictureIntent.resolveActivity(mApp.getPackageManager()) != null) {
            mSendImageFragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private Uri createImageUri() {
        try {
            return ImageUtil.getTempImageUri(mApp);
        } catch (IOException e) {
            //sLogger.w("Can't create file to take picture!");

            throw Exceptions.propagate(e);
        }
    }

    private SendImageMessagePresenter createPresenter() {
        SendImageMessagePresenter presenter = new SendImageMessagePresenter();

        presenter.addView(this);

        return presenter;
    }

    private PointGeometry createPointGeometry(LocationSample locationSample) {
        PointGeometry pointGeometry = null;

        if (locationSample != null) {
            com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry
                    location = locationSample.getLocation();

            pointGeometry = new PointGeometry(location.latitude, location.longitude);
        }

        return pointGeometry;
    }

    private com.teamagam.gimelgimel.domain.messages.entity.MessageImage createMessageImage(String senderId,
                                                                                          PointGeometry geometry,
                                                                                          String imagePath) {
        ImageMetadata metadata =
                new ImageMetadata(System.currentTimeMillis(), imagePath, IMAGE_SOURCE);

        if (geometry != null) {
            metadata.setLocation(geometry);
        }

        com.teamagam.gimelgimel.domain.messages.entity.MessageImage message =
                new com.teamagam.gimelgimel.domain.messages.entity.MessageImage(senderId, metadata);

        return message;
    }

    private LocationSample getLocationSample() {
        return mLocationFetcher.getLastKnownLocation();
    }
}

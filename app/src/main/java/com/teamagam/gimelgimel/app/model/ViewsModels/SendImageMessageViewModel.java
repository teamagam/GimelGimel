package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.teamagam.gimelgimel.app.view.fragments.SendImageFragment;
import com.teamagam.gimelgimel.domain.image.GetImagePathInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SendImageMessageInteractorFactory;
import com.teamagam.gimelgimel.presentation.presenters.base.SimpleSubscriber;

import javax.inject.Inject;

public class SendImageMessageViewModel {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Inject
    SendImageMessageInteractorFactory mInteractorFactory;

    @Inject
    GetImagePathInteractorFactory mImagePathInteractorFactory;

    private SendImageFragment mSendImageFragment;

    @Inject
    public SendImageMessageViewModel() {

    }

    public void setView(SendImageFragment sendImageFragment) {
        mSendImageFragment = sendImageFragment;
    }

//    @Override
//    public void displaySuccessfulMessageStatus() {
//        showInfoSnackbar("The image has been sent");
//    }
//
//    public void showProgress() {
//        showInfoSnackbar("Sending image");
//    }


//    @Override
//    public void showError(String message) {
//        //sLogger.e(message);
//        showErrorSnackbar("An error has occurred while sending image: " + message);
//    }

    @SuppressWarnings("UnusedParameters")
    public void onFabClicked(View v) {
        takePicture();
    }

    private void takePicture() {
//        try {
//        sLogger.userInteraction("Start camera activity button clicked");

        mImagePathInteractorFactory.create(new SimpleSubscriber<String>() {
            @Override
            public void onNext(String uriString) {
                Uri imageUri = Uri.parse(uriString);
                startCameraIntent(imageUri);
            }
        }).execute();

//        } catch (Exception e) {
//            showErrorSnackbar("Problem with taking images - Couldn't create temp file");
//        }
    }

    private void startCameraIntent(Uri mImageUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        if (takePictureIntent.resolveActivity(
                mSendImageFragment.getActivity().getPackageManager()) != null) {
            mSendImageFragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void sendImage() {
        long imageTime = System.currentTimeMillis();
        mInteractorFactory.create(imageTime).execute();
    }

}

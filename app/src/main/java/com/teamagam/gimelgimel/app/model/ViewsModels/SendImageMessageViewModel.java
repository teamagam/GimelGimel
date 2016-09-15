package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.utils.ImageUtil;
import com.teamagam.gimelgimel.app.view.fragments.SendImageFragment;
import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class SendImageMessageViewModel implements SendImageMessagePresenter.View {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Inject
    SendImageMessagePresenter mPresenter;

    @Inject
    SendImageMessageInteractor mInteractor;

    private Uri mImageUri;
    private WeakReference<SendImageFragment> mSendImageFragment;
    private GGApplication mApp;

    public SendImageMessageViewModel(SendImageFragment sendImageFragment) {
        mSendImageFragment = new WeakReference<>(sendImageFragment);
        mApp = (GGApplication) sendImageFragment.getActivity().getApplicationContext();

        mApp.getMessagesComponent().inject(this);

        mPresenter.addView(this);
    }

    @Override
    public void displaySuccessfulMessageStatus() {
        showInfoSnackbar("The image has been sent");
    }

    @Override
    public void showProgress() {
        showInfoSnackbar("Sending image");
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showError(String message) {
        //sLogger.e(message);
        showErrorSnackbar("An error has occurred while sending image: " + message);
    }

    public void onFabClick(View view) {
        takePicture();
    }

    public void sendImage() {
        showProgress();

        String imagePath = mImageUri.getPath();
        mInteractor.sendImageMessage(mPresenter.createSubscriber(), imagePath, System.currentTimeMillis());
    }

    private void takePicture() {
        try {
            //sLogger.userInteraction("Start camera activity button clicked");

            mImageUri = createImageUri();
            startCameraIntent(mImageUri);
        } catch (Exception e) {
            showErrorSnackbar("Problem with taking images - Couldn't create temp file");
        }
    }

    private void startCameraIntent(Uri mImageUri) {
        SendImageFragment fragment = mSendImageFragment.get();

        if(fragment != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

            if (takePictureIntent.resolveActivity(mApp.getPackageManager()) != null) {
                fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private Uri createImageUri() throws IOException {
        try {
            return ImageUtil.getTempImageUri(mApp);
        } catch (IOException e) {
            //sLogger.w("Can't create file to take picture!");

            throw e;
        }
    }

    private void showInfoSnackbar(String text) {
        showSnackbar(text, R.color.colorPrimary);
    }

    private void showErrorSnackbar(String text) {
        showSnackbar(text, R.color.red);
    }

    private void showSnackbar(String text, int colorPrimary) {
        SendImageFragment fragment = mSendImageFragment.get();

        if(fragment != null) {
            Snackbar snackbar = Snackbar.make(fragment.getView(), text, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(mApp, colorPrimary));

            snackbar.show();
        }
    }
}

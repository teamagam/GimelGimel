package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.teamagam.gimelgimel.app.message.view.SendImageFragment;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.image.GetImagePathInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SendImageMessageInteractorFactory;

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

    public void onFabClicked() {
        takePicture();
    }

    public void sendImage() {
        long imageTime = System.currentTimeMillis();
        mInteractorFactory.create(imageTime).execute();
    }

    private void takePicture() {
        mImagePathInteractorFactory.create(new SimpleSubscriber<String>() {
            @Override
            public void onNext(String uriString) {
                Uri imageUri = Uri.parse(uriString);
                startCameraIntent(imageUri);
            }
        }).execute();
    }

    private void startCameraIntent(Uri mImageUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        if (takePictureIntent.resolveActivity(
                mSendImageFragment.getActivity().getPackageManager()) != null) {
            mSendImageFragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}

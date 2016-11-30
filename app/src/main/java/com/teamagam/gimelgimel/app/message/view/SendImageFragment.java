package com.teamagam.gimelgimel.app.message.view;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.message.viewModel.SendImageMessageViewModel;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseFragment;
import com.teamagam.gimelgimel.data.images.ImageUtils;
import com.teamagam.gimelgimel.databinding.FragmentSendImageBinding;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendImageFragment extends BaseFragment<GGApplication> {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String CAPTURED_IMAGE_PATH_KEY = "captured_image";

    @Inject
    SendImageMessageViewModel mViewModel;

    private String mCapturedImagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCapturedImagePath = savedInstanceState.getString(CAPTURED_IMAGE_PATH_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        FragmentSendImageBinding binding = FragmentSendImageBinding.bind(view);

        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);

        mViewModel.setView(this);
        binding.setViewModel(mViewModel);

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CAPTURED_IMAGE_PATH_KEY, mCapturedImagePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (isImageCaptured(resultCode)) {
                sLogger.userInteraction("Sending camera activity result");

                mViewModel.sendImage(mCapturedImagePath);
            } else {
                sLogger.userInteraction("Camera activity returned with no captured image");
            }
        }
    }

    public void takePicture() {
        Uri newImageUri = createNewImageUri();
        mCapturedImagePath = newImageUri.getPath();
        startCameraIntent(newImageUri);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_send_image;
    }


    private Uri createNewImageUri() {
        return ImageUtils.getTempImageUri(getContext());
    }

    private void startCameraIntent(Uri localImageUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, localImageUri);

        if (takePictureIntent.resolveActivity(
                getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private boolean isImageCaptured(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }
}

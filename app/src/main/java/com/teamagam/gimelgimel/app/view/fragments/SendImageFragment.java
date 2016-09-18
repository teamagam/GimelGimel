package com.teamagam.gimelgimel.app.view.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.SendImageMessageViewModel;
import com.teamagam.gimelgimel.databinding.FragmentSendImageBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendImageFragment extends BaseFragment<GGApplication> {

    SendImageMessageViewModel mViewModel;

    public SendImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentSendImageBinding binding = FragmentSendImageBinding.inflate(inflater, container, false);

        mViewModel = new SendImageMessageViewModel(this);
        binding.setViewModel(mViewModel);

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SendImageMessageViewModel.REQUEST_IMAGE_CAPTURE) {
            if (isImageCaptured(resultCode)) {
                sLogger.userInteraction("Sending camera activity result");

                mViewModel.sendImage();
            } else {
                sLogger.userInteraction("Camera activity returned with no captured image");
            }
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_send_image;
    }

    private boolean isImageCaptured(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }
}

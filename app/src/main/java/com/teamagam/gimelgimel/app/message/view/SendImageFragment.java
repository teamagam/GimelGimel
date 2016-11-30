package com.teamagam.gimelgimel.app.message.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.view.GGApplication;
import com.teamagam.gimelgimel.app.message.viewModel.SendImageMessageViewModel;
import com.teamagam.gimelgimel.app.mainActivity.MainActivity;
import com.teamagam.gimelgimel.app.common.view.fragments.BaseFragment;
import com.teamagam.gimelgimel.databinding.FragmentSendImageBinding;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendImageFragment extends BaseFragment<GGApplication> {

    @Inject
    SendImageMessageViewModel mViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        FragmentSendImageBinding binding = FragmentSendImageBinding.bind(view);

        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);

        mViewModel.setView(this);
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

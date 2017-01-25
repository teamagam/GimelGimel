package com.teamagam.gimelgimel.app.message.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.message.viewModel.SendMessagesViewModel;
import com.teamagam.gimelgimel.data.images.ImageUtils;
import com.teamagam.gimelgimel.databinding.FragmentSendMessagesBinding;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessagesFragment extends BaseDataFragment<SendMessagesViewModel> {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String CAPTURED_IMAGE_PATH_KEY = "captured_image";

    @Inject
    SendMessagesViewModel mViewModel;

    @BindView(R.id.send_text_message_fab)
    FloatingActionButton mSendTextFab;

    private String mCapturedImagePath;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCapturedImagePath = savedInstanceState.getString(CAPTURED_IMAGE_PATH_KEY);
        }
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

    public void setSendTextFabClickable(boolean value) {
        mSendTextFab.setClickable(value);
        mSendTextFab.setEnabled(value);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_send_messages;
    }

    @Override
    protected SendMessagesViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentSendMessagesBinding bind = FragmentSendMessagesBinding.bind(rootView);

        bind.setViewModel(mViewModel);
        mViewModel.setView(this);

        return bind;
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

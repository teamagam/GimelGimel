package com.teamagam.gimelgimel.app.message.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataBindingFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.message.viewModel.SendMessagesViewModel;
import com.teamagam.gimelgimel.data.images.ImageUtils;
import com.teamagam.gimelgimel.databinding.FragmentSendMessagesBinding;
import javax.inject.Inject;

public class SendMessagesFragment extends BaseDataBindingFragment<SendMessagesViewModel> {

  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final String CAPTURED_IMAGE_PATH_KEY = "captured_image";

  @Inject
  SendMessagesViewModel mViewModel;

  @Inject
  ImageUtils mImageUtils;

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
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    Drawable sendDrawable =
        ContextCompat.getDrawable(getContext(), android.R.drawable.ic_menu_send);
    sendDrawable.setAutoMirrored(true);
    mSendTextFab.setImageDrawable(sendDrawable);
    return view;
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

  public void setSendTextFabClickable(boolean isClickable) {
    mSendTextFab.setClickable(isClickable);
    mSendTextFab.setEnabled(isClickable);
  }

  public void hideKeyboard() {
    if (getView() != null) {
      InputMethodManager imm =
          (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

      sLogger.v("Hid keyboard");
    }
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
    return mImageUtils.getTempImageUri();
  }

  private void startCameraIntent(Uri localImageUri) {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, localImageUri);

    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  private boolean isImageCaptured(int resultCode) {
    return resultCode == Activity.RESULT_OK;
  }
}

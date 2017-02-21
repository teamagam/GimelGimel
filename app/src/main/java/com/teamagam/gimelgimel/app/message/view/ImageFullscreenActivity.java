package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * A Image full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageFullscreenActivity extends BaseActivity<GGApplication> {

    @Inject
    GlideLoader mGlideLoader;

    @BindView(R.id.image_fullscreen_view)
    ImageViewTouch mImageView;

    @BindView(R.id.fullscreen_progress_view)
    CircularProgressView mProgressBar;

    private boolean mIsControlsVisible;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ImageFullscreenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        mApp.getApplicationComponent().inject(this);

        Uri imageUri = getIntent().getData();
        mGlideLoader.loadImage(imageUri, mImageView, mProgressBar);

        setViewTapListener();
        hideControls();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_image_fullscreen;
    }

    private void setViewTapListener() {
        // Set up the user interaction to manually showControls or hideControls the system UI.
        mImageView.setSingleTapListener(
                new ImageViewTouch.OnImageViewTouchSingleTapListener() {
                    @Override
                    public void onSingleTapConfirmed() {
                        toggleControlsVisibility();
                    }
                });
    }

    private void toggleControlsVisibility() {
        if (mIsControlsVisible) {
            hideControls();
        } else {
            showControls();
        }
    }

    private void hideControls() {
        mImageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mIsControlsVisible = false;
    }

    private void showControls() {
        // Show the system barO
        mImageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mIsControlsVisible = true;
    }
}

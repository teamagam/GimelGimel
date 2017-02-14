package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;

/**
 * A Image full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageFullscreenActivity extends BaseActivity<GGApplication> {

    @BindView(R.id.image_fullscreen_view)
    ImageViewTouch mImageView;

    private boolean mIsControlsVisible;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ImageFullscreenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        Uri imageUri = getIntent().getData();

        loadImage(imageUri);
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
        // Show the system bar
        mImageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mIsControlsVisible = true;
    }


    private void loadImage(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .fitCenter()
                .placeholder(new IndeterminateProgressDrawable(this))
                .crossFade()
                .into(mImageView);
    }
}

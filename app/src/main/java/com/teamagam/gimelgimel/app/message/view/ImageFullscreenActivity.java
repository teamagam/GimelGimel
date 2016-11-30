package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.view.GGApplication;
import com.teamagam.gimelgimel.app.common.view.BaseActivity;
import com.teamagam.gimelgimel.app.common.drawable.CircleProgressBarDrawable;
import com.teamagam.gimelgimel.app.common.drawable.ZoomableDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Image full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageFullscreenActivity extends BaseActivity<GGApplication> {

    @BindView(R.id.image_fullscreen_view)
    ZoomableDraweeView mDraweeView;

    private boolean mIsControlsVisible;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ImageFullscreenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        Uri imageUri = getIntent().getData();

        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
        mDraweeView.setImageURI(imageUri);
        setViewTapListener();
        hideControls();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_image_fullscreen;
    }

    private void setViewTapListener() {
        // Set up the user interaction to manually showControls or hideControls the system UI.
        mDraweeView.setTapListener(
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        toggleControlsVisibility();
                        return true;
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
        mDraweeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mIsControlsVisible = false;
    }

    private void showControls() {
        // Show the system bar
        mDraweeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mIsControlsVisible = true;
    }

}

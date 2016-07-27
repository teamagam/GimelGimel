package com.teamagam.gimelgimel.app.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.view.drawable.CircleProgressBarDrawable;
import com.teamagam.gimelgimel.app.view.drawable.ZoomableDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Image full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageFullscreenActivity extends BaseActivity<GGApplication> {

    @BindView(R.id.image_fullscreen_view)
    ZoomableDraweeView mDraweeView;

    private boolean mVisible;

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
                        toggle();
                        return true;
                    }
                });
    }

    private void toggle() {
        if (mVisible) {
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

        mVisible = false;
    }

    private void showControls() {
        // Show the system bar
        mDraweeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mVisible = true;
    }

}

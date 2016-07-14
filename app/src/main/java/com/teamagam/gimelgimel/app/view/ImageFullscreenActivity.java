package com.teamagam.gimelgimel.app.view;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

    private static final String IMAGE_URI_KEY = "IMAGE_URI";
    private static final int UI_ANIMATION_DELAY = 300;

    private boolean mVisible;
    private Uri mImageUri;

    private final Handler mHideHandler = new Handler();
    private Runnable mHideRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        setViewTapListener();
        initRunnables();

        mImageUri = getIntent().getData();
        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
        mDraweeView.setImageURI(mImageUri);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hideControls() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(UI_ANIMATION_DELAY);
    }

    private void setViewTapListener() {
        // Set up the user interaction to manually showControls or hideControls the system UI.
        mDraweeView.setTapListener(
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        toggle();
                        return true;
                    }
                });
    }

    private void initRunnables() {
        mHideRunnable = new Runnable() {
            @Override
            public void run() {
                hideControls();
            }
        };
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

    @SuppressLint("InlinedApi")
    private void showControls() {
        // Show the system bar
        mDraweeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mVisible = true;
    }

    /**
     * Schedules a call to hideControls() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI_KEY, mImageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageUri = (Uri) savedInstanceState.get(IMAGE_URI_KEY);
        mDraweeView.setImageURI(mImageUri);
    }


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_image_fullscreen;
    }
}

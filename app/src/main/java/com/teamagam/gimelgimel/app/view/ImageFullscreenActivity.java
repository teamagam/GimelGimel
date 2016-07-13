package com.teamagam.gimelgimel.app.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.view.drawable.CircleProgressBarDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Image full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageFullscreenActivity extends BaseActivity<GGApplication> {

    @BindView(R.id.image_fullscreen_view)
    SimpleDraweeView mDraweeView;

    private static final String IMAGE_URI_KEY = "IMAGE_URI";

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        mImageUri = getIntent().getData();

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        mDraweeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());

        mDraweeView.setImageURI(mImageUri);

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

package com.teamagam.gimelgimel.app.view;

import android.app.Application;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.teamagam.gimelgimel.R;

public abstract class BaseActivity<T extends Application> extends AppCompatActivity {

    protected final String TAG = ((Object) this).getClass().getSimpleName();

    protected T mApp;

    protected boolean mIsResumed = false;

    protected boolean mIsTwoPane;

    protected Location mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Just inflate the activity layout
        int layoutResId = getActivityLayout();
        if (layoutResId > 0) {
            setContentView(layoutResId);
        }

        // Set application object reference
        mApp = (T) getApplication();
        mIsTwoPane = getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsResumed = false;
    }

    /***
     * This method helps setting the layout of the activity
     *
     * @return The resource Id of the layout you wish to inflate (i.e. R.layout.activity_layout)
     */
    protected abstract int getActivityLayout();
}
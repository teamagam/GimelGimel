package com.teamagam.gimelgimel.app.view;

import android.app.Application;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;

public abstract class BaseActivity<T extends Application> extends AppCompatActivity {

    protected final String TAG = ((Object) this).getClass().getSimpleName();

    protected final Logger sLogger = LoggerFactory.create(((Object) this).getClass());

    protected T mApp;

    protected boolean mIsResumed = false;

    protected boolean mIsTwoPane;

    @Override
    public void onBackPressed() {
        sLogger.userInteraction("Back key pressed");
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sLogger.onCreate();
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
    protected void onStart() {
        sLogger.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        sLogger.onResume();
        super.onResume();

        mIsResumed = true;
    }

    @Override
    protected void onPause() {
        sLogger.onPause();
        super.onPause();

        mIsResumed = false;
    }

    @Override
    protected void onStop() {
        sLogger.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        sLogger.onDestroy();
        super.onDestroy();
    }

    /***
     * This method helps setting the layout of the activity
     *
     * @return The resource Id of the layout you wish to inflate (i.e. R.layout.activity_layout)
     */
    protected abstract int getActivityLayout();
}
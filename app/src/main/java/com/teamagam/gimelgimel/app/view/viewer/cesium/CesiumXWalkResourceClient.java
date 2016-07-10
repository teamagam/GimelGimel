package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.util.Log;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

/**
 * Created on 7/10/2016.
 * TODO: complete text
 */
public class CesiumXWalkResourceClient extends XWalkResourceClient {

    private boolean mIsLoadFromURL = false;

    public CesiumXWalkResourceClient(XWalkView view) {
        super(view);
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        super.onLoadFinished(view, url);
        Log.d("XWALK RES CLIENT", "load finished");
        setShouldNotLoadURL();
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        super.shouldOverrideUrlLoading(view, url);
        return mIsLoadFromURL;
    }

    public void setShouldNotLoadURL(){
        mIsLoadFromURL = true;
    }

    public void setShouldLoadURL(){
        mIsLoadFromURL = false;
    }

    @Override
    public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
        Log.d("onReceivedLoadError", "i: "+ errorCode + description + " " + failingUrl);
    }
}

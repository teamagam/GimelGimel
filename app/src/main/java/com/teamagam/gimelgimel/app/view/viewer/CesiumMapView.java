package com.teamagam.gimelgimel.app.view.viewer;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.teamagam.gimelgimel.BuildConfig;

/**
 * TODO: document your custom view class.
 */
public class CesiumMapView extends WebView {

    public static final String FILE_ANDROID_ASSET_VIEWER = "file:///android_asset/cesiumHelloWorld.html";

    public CesiumMapView(Context context) {
        super(context);
        init(null, 0);
    }

    public CesiumMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CesiumMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        WebSettings thisWebSettings = getSettings();
        thisWebSettings.setAllowUniversalAccessFromFileURLs(true);
        thisWebSettings.setAllowFileAccessFromFileURLs(true);
        thisWebSettings.setJavaScriptEnabled(true);

        thisWebSettings.setUseWideViewPort(true);
        thisWebSettings.setLoadWithOverviewMode(true);
        setWebViewClient(new WebViewClient());


        //For debug only
        if(BuildConfig.DEBUG) {
            setWebContentsDebuggingEnabled(true);
        }


        this.loadUrl(FILE_ANDROID_ASSET_VIEWER);
//        this.loadUrl("http://www.google.com");
    }







}

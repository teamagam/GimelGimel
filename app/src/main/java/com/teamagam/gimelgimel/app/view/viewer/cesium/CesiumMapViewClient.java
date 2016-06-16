package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A {@link WebViewClient} to control our {@link CesiumMapView}
 */
public class CesiumMapViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return true;
    }
}

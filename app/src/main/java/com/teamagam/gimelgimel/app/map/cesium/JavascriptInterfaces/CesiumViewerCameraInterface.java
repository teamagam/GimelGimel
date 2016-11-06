package com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces;

import com.teamagam.gimelgimel.app.map.cesium.CesiumUtils;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import org.xwalk.core.JavascriptInterface;

import rx.subjects.PublishSubject;

/**
 * Javascript Interface for Cesium Viewer to update it's camera state for the app to manage
 */
public class CesiumViewerCameraInterface {

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumViewerCameraInterface";

    private PublishSubject<ViewerCamera> mViewerCameraPublishSubject;

    public CesiumViewerCameraInterface() {
        mViewerCameraPublishSubject = PublishSubject.create();
    }

    public rx.Observable<ViewerCamera> getViewerCameraObservable() {
        return mViewerCameraPublishSubject.replay(1).autoConnect();
    }

    @JavascriptInterface
    public void updateViewerCamera(String cameraJson) {
        ViewerCamera vc = CesiumUtils.getViewerCameraFromJson(cameraJson);
        mViewerCameraPublishSubject.onNext(vc);
    }
}

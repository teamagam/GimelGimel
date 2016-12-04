package com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces;

import com.teamagam.gimelgimel.app.map.cesium.CesiumUtils;
import com.teamagam.gimelgimel.data.base.repository.SingleReplayRepository;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import org.xwalk.core.JavascriptInterface;

/**
 * Javascript Interface for Cesium Viewer to update it's camera state for the app to manage
 */
public class CesiumViewerCameraInterface {

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumViewerCameraInterface";

    private final SingleReplayRepository<ViewerCamera> mViewerCameraInnerRepo;

    public CesiumViewerCameraInterface() {
        mViewerCameraInnerRepo = new SingleReplayRepository<>();
    }

    public rx.Observable<ViewerCamera> getViewerCameraObservable() {
        return mViewerCameraInnerRepo.getObservable();
    }

    @JavascriptInterface
    public void updateViewerCamera(String cameraJson) {
        ViewerCamera vc = CesiumUtils.getViewerCameraFromJson(cameraJson);
        mViewerCameraInnerRepo.setValue(vc);
    }
}

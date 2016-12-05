package com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces;

import com.teamagam.gimelgimel.app.map.cesium.CesiumUtils;
import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import org.xwalk.core.JavascriptInterface;

/**
 * Javascript Interface for Cesium Viewer to update it's camera state for the app to manage
 */
public class CesiumViewerCameraInterface {

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumViewerCameraInterface";

    private final ReplayRepository<ViewerCamera> mViewerCameraInnerRepo;

    public CesiumViewerCameraInterface() {
        mViewerCameraInnerRepo = ReplayRepository.createReplayCount(1);
    }

    public rx.Observable<ViewerCamera> getViewerCameraObservable() {
        return mViewerCameraInnerRepo.getObservable();
    }

    @JavascriptInterface
    public void updateViewerCamera(String cameraJson) {
        ViewerCamera vc = CesiumUtils.getViewerCameraFromJson(cameraJson);
        mViewerCameraInnerRepo.add(vc);
    }
}

package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class ViewerCameraDataRepository implements ViewerCameraRepository {

    private ViewerCamera mViewerCamera;

    @Inject
    public ViewerCameraDataRepository() {
        mViewerCamera = getDefaultViewerCamera();
    }

    @Override
    public Observable<ViewerCamera> getObservable() {
        return Observable.just(mViewerCamera);
    }

    @Override
    public void set(ViewerCamera viewerCamera) {
        mViewerCamera = viewerCamera;
    }

    private static ViewerCamera getDefaultViewerCamera() {

        PointGeometry cameraPosition = new PointGeometry(31.8, 34.5, 80000);
        float heading = 0.0f;
        float pitch = (float) -(Math.PI / 2);
        float roll = 0.0f;
        return new ViewerCamera(cameraPosition, heading, pitch, roll);
    }
}

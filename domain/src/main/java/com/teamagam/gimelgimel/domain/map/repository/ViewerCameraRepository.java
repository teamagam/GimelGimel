package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import rx.Observable;

/**
 * Repository that holds for the portion of the map that is displayed.
 */

public interface ViewerCameraRepository {

    Observable<ViewerCamera> getObservable();

    void set(ViewerCamera frustum);

}

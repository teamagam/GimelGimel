package com.teamagam.gimelgimel.domain.map.repository;


import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;

import rx.Observable;

public interface CurrentIntermediateRasterRepository {
    Observable<DisplayEvent> getIntermediateRasterEventsObservable();

    IntermediateRaster getCurrentIntermediateRaster();

    void setCurrentIntermediateRaster(IntermediateRaster raster);

    enum DisplayEvent {
        DISPLAY, HIDE
    }
}

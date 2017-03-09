package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;
import com.teamagam.gimelgimel.domain.map.repository.CurrentIntermediateRasterRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class CurrentIntermediateRasterDataRepository implements CurrentIntermediateRasterRepository {

    private ReplayRepository<DisplayEvent> mInnerRepo;
    private IntermediateRaster mIntermediateRaster;

    @Inject
    public CurrentIntermediateRasterDataRepository() {
        mInnerRepo = ReplayRepository.createReplayCount(1);
        mIntermediateRaster = null;
    }

    @Override
    public Observable<DisplayEvent> getIntermediateRasterEventsObservable() {
        return mInnerRepo.getObservable();
    }

    @Override
    public IntermediateRaster getCurrentIntermediateRaster() {
        return mIntermediateRaster;
    }

    @Override
    public void setCurrentIntermediateRaster(IntermediateRaster raster) {
        mIntermediateRaster = raster;
        mInnerRepo.add(
                mIntermediateRaster == null ? DisplayEvent.HIDE : DisplayEvent.DISPLAY);
    }
}

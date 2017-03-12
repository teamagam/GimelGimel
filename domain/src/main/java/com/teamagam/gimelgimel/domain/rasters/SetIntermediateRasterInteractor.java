package com.teamagam.gimelgimel.domain.rasters;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class SetIntermediateRasterInteractor extends BaseDataInteractor {

    private final IntermediateRaster mIntermediateRaster;
    private final IntermediateRasterVisibilityRepository mVisibilityRepository;

    public SetIntermediateRasterInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided IntermediateRasterVisibilityRepository visibilityRepository,
            IntermediateRaster intermediateRaster) {
        super(threadExecutor);
        mVisibilityRepository = visibilityRepository;
        mIntermediateRaster = intermediateRaster;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        SubscriptionRequest setRasterRequest = factory.create(
                Observable.just(mIntermediateRaster)
                        .doOnNext(this::removeOldAndSetNew));
        return Collections.singletonList(setRasterRequest);
    }

    private void removeOldAndSetNew(IntermediateRaster raster) {
        removeOld();
        setNew(raster);
    }

    private void removeOld() {
        String currentlyVisibleName = mVisibilityRepository.getCurrentlyVisibleName();
        if (currentlyVisibleName != null) {
            mVisibilityRepository.addChange(
                    new IntermediateRasterVisibilityChange(false, currentlyVisibleName));
        }
    }

    private void setNew(IntermediateRaster raster) {
        mVisibilityRepository.addChange(
                new IntermediateRasterVisibilityChange(true, raster.getName()));
    }
}

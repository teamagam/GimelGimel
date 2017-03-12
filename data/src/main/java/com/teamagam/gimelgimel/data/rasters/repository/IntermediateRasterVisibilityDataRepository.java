package com.teamagam.gimelgimel.data.rasters.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;

import java.util.Objects;

import javax.inject.Inject;

import rx.Observable;

public class IntermediateRasterVisibilityDataRepository implements IntermediateRasterVisibilityRepository {

    private final ReplayRepository<IntermediateRasterVisibilityChange> mInnerRepo;
    private String mCurrentlySelectedName;

    @Inject
    public IntermediateRasterVisibilityDataRepository() {
        mInnerRepo = ReplayRepository.createReplayAll();
        mCurrentlySelectedName = null;
    }

    @Override
    public Observable<IntermediateRasterVisibilityChange> getChangesObservable() {
        return mInnerRepo.getObservable();
    }

    @Override
    public void addChange(IntermediateRasterVisibilityChange change) {
        updateCurrentlySelected(change);
        mInnerRepo.add(change);
    }

    @Override
    public String getCurrentlyVisibleName() {
        return mCurrentlySelectedName;
    }

    private void updateCurrentlySelected(IntermediateRasterVisibilityChange change) {
        throwOnMultipleSelection(change);

        if (isCurrentUnselected(change)) {
            mCurrentlySelectedName = null;
        } else if (change.isVisible()) {
            mCurrentlySelectedName = change.getIntermediateRasterName();
        }
    }

    private void throwOnMultipleSelection(IntermediateRasterVisibilityChange change) {
        if (change.isVisible() && mCurrentlySelectedName != null) {
            throw new IllegalStateException("Selecting multiple intermediate rasters is forbidden");
        }
    }

    private boolean isCurrentUnselected(IntermediateRasterVisibilityChange change) {
        return !change.isVisible() && Objects.equals(mCurrentlySelectedName,
                change.getIntermediateRasterName());
    }
}

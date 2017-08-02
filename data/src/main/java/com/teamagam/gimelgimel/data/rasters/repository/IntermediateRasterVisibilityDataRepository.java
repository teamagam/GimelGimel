package com.teamagam.gimelgimel.data.rasters.repository;

import com.teamagam.gimelgimel.data.base.visibility.VisibilityDataRepository;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import java.util.Objects;
import javax.inject.Inject;

public class IntermediateRasterVisibilityDataRepository
    extends VisibilityDataRepository<IntermediateRasterVisibilityChange>
    implements IntermediateRasterVisibilityRepository {

  private String mCurrentlySelectedName;

  @Inject
  public IntermediateRasterVisibilityDataRepository() {
    super();
    mCurrentlySelectedName = null;
  }

  @Override
  public void addChange(IntermediateRasterVisibilityChange change) {
    super.addChange(change);
    updateCurrentlySelected(change);
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
      mCurrentlySelectedName = change.getName();
    }
  }

  private void throwOnMultipleSelection(IntermediateRasterVisibilityChange change) {
    if (change.isVisible() && mCurrentlySelectedName != null) {
      throw new IllegalStateException("Selecting multiple intermediate rasters is forbidden");
    }
  }

  private boolean isCurrentUnselected(IntermediateRasterVisibilityChange change) {
    return !change.isVisible() && Objects.equals(mCurrentlySelectedName, change.getName());
  }
}

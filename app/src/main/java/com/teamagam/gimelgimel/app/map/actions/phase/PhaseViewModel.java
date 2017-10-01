package com.teamagam.gimelgimel.app.map.actions.phase;

import com.google.auto.factory.AutoFactory;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.phase.DisplayPhaseLayerInteractor;
import com.teamagam.gimelgimel.domain.phase.DisplayPhaseLayerInteractorFactory;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import javax.inject.Inject;

@AutoFactory
public class PhaseViewModel extends BaseViewModel {

  private final DisplayPhaseLayerInteractorFactory mDisplayPhaseLayerInteractorFactory;
  private final PhasesDisplayer mPhasesDisplayer;
  private final String mPhaseLayerId;

  private DisplayPhaseLayerInteractor mDisplayInteractor;

  @Inject
  public PhaseViewModel(DisplayPhaseLayerInteractorFactory displayPhaseLayerInteractorFactory,
      PhasesDisplayer phasesDisplayer,
      String phaseLayerId) {
    mPhasesDisplayer = phasesDisplayer;
    mPhaseLayerId = phaseLayerId;
    mDisplayPhaseLayerInteractorFactory = displayPhaseLayerInteractorFactory;
  }

  @Override
  public void start() {
    super.start();
    mDisplayInteractor =
        mDisplayPhaseLayerInteractorFactory.create(this::displayPhaseLayer, mPhaseLayerId);
    execute(mDisplayInteractor);
  }

  @Override
  public void stop() {
    super.stop();
    unsubscribe(mDisplayInteractor);
  }

  public void onPhaseSelected(int phasePosition) {
    //draw phase on map
    mPhasesDisplayer.displayPhase(phasePosition);
  }

  private void displayPhaseLayer(PhaseLayer layer) {
    //display name, description, timestamp(?)
    displayPhases(layer);
  }

  private void displayPhases(PhaseLayer layer) {
    int position = 0;
    for (DynamicLayer phase : layer.getPhases()) {
      mPhasesDisplayer.addPhase(phase, position++);
    }
  }

  interface PhasesDisplayer {
    void addPhase(DynamicLayer phase, int position);

    void displayPhase(int position);
  }
}

package com.teamagam.gimelgimel.app.map.actions.phase;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.phase.DisplayPhaseLayerInteractor;
import com.teamagam.gimelgimel.domain.phase.DisplayPhaseLayerInteractorFactory;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import java.util.List;

@AutoFactory
public class PhaseViewModel extends BaseViewModel {

  private static final AppLogger sLogger = AppLoggerFactory.create();

  private final DisplayPhaseLayerInteractorFactory mDisplayPhaseLayerInteractorFactory;
  private final PhasesDisplayer mPhasesDisplayer;
  private final String mPhaseLayerId;

  private DisplayPhaseLayerInteractor mDisplayInteractor;

  public PhaseViewModel(
      @Provided DisplayPhaseLayerInteractorFactory displayPhaseLayerInteractorFactory,
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

  public void onPhaseEntityClicked(DynamicEntity dynamicEntity) {

  }

  private void displayPhaseLayer(PhaseLayer layer) {
    //display name, description, timestamp(?)
    sLogger.i("Displaying phase layer " + layer);
    displayPhases(layer);
  }

  private void displayPhases(PhaseLayer layer) {
    List<DynamicLayer> phases = layer.getPhases();

    sLogger.i("Displaying " + phases.size() + " phases");
    for (int i = 0; i < phases.size(); i++) {
      mPhasesDisplayer.addPhase(phases.get(i), i);
    }
  }

  interface PhasesDisplayer {
    void addPhase(DynamicLayer phase, int position);

    void displayPhase(int position);
  }
}

package com.teamagam.gimelgimel.app.map.actions.phase;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.DynamicLayersMapDisplayer;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerPresentation;
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
  private final GGMapView mGGMapView;
  private final DynamicLayersMapDisplayer mMapDisplayer;
  private DisplayPhaseLayerInteractor mDisplayInteractor;
  private PhaseLayer mPhaseLayer;
  private DynamicLayer mCurrentlyDisplayedPhase;

  public PhaseViewModel(
      @Provided DisplayPhaseLayerInteractorFactory displayPhaseLayerInteractorFactory,
      GGMapView GGMapView,
      PhasesDisplayer phasesDisplayer,
      String phaseLayerId) {
    mPhasesDisplayer = phasesDisplayer;
    mPhaseLayerId = phaseLayerId;
    mDisplayPhaseLayerInteractorFactory = displayPhaseLayerInteractorFactory;
    mGGMapView = GGMapView;
    mMapDisplayer = new DynamicLayersMapDisplayer(mGGMapView);
  }

  @Override
  public void start() {
    super.start();
    mDisplayInteractor =
        mDisplayPhaseLayerInteractorFactory.create(this::displayPhaseLayer, mPhaseLayerId);
    execute(mDisplayInteractor);
    displayFirstPhaseOnMap();
  }

  @Override
  public void stop() {
    super.stop();
    unsubscribe(mDisplayInteractor);
  }

  public void onPhaseSelected(int phasePosition) {
    DynamicLayer phase = mPhaseLayer.getPhase(phasePosition);
    displayPhaseOnMap(phase);
  }

  private void displayPhaseOnMap(DynamicLayer phase) {
    clearOldPhase();
    showPhase(phase);
  }

  private void clearOldPhase() {
    if (mCurrentlyDisplayedPhase != null) {
      hidePhase();
    }
  }

  private void hidePhase() {
    mMapDisplayer.display(new DynamicLayerPresentation(mCurrentlyDisplayedPhase, false));
    mCurrentlyDisplayedPhase = null;
  }

  private void showPhase(DynamicLayer phase) {
    mMapDisplayer.display(new DynamicLayerPresentation(phase, true));
    mCurrentlyDisplayedPhase = phase;
  }

  public void onPhaseEntityClicked(DynamicEntity dynamicEntity) {

  }

  private void displayPhaseLayer(PhaseLayer layer) {
    //display name, description, timestamp(?)
    sLogger.i("Displaying phase layer " + layer);
    mPhaseLayer = layer;
    displayPhases(layer);
  }

  private void displayFirstPhaseOnMap() {
    mGGMapView.setOnReadyListener(() -> {
      if (mPhaseLayer != null) {
        displayPhaseOnMap(mPhaseLayer.getPhase(0));
      }
    });
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

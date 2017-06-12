package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

@AutoFactory
public class SendGeometryViewModel extends BaseMapViewModel {

  private static AppLogger sLogger = AppLoggerFactory.create();

  private String mDescription;

  protected SendGeometryViewModel(
      @Provided
          DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided
          DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      GGMapView ggMapView) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);
  }

  public void onSendFabClicked() {
    sLogger.userInteraction("Send geometry fab clicked");
  }

  public void onUndoClicked() {
    sLogger.userInteraction("Send geometry undo clicked");
  }

  public void onSwitchChanged(boolean isChecked) {
    sLogger.userInteraction("Send geometry switch changed to " + isChecked);
  }

  public void setDescription(String description) {
    sLogger.userInteraction("Send geometry description changed to " + description);
    mDescription = description;
  }

  public String getDescription() {
    return mDescription;
  }

  public void onLocationSelection(PointGeometry point) {
    sLogger.userInteraction(
        "Send geometry location selected " + point.getLatitude() + "/" + point.getLongitude());
  }
}
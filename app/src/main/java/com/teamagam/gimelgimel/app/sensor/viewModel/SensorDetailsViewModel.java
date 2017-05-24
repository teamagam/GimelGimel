package com.teamagam.gimelgimel.app.sensor.viewModel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.DisplaySensorsInteractor;
import com.teamagam.gimelgimel.domain.sensors.DisplaySensorsInteractorFactory;
import javax.inject.Inject;

public class SensorDetailsViewModel extends BaseViewModel {

  @Inject
  DisplaySensorsInteractorFactory mSensorsDisplayInteractorFactory;

  private String mSensorName;
  private PointGeometryApp mSensorLocation;
  private boolean mIsAnySelected;
  private DisplaySensorsInteractor mDisplaySensorsInteractor;

  @Inject
  SensorDetailsViewModel() {
    mSensorLocation = PointGeometryApp.DEFAULT_POINT;
    mSensorName = "";
    mIsAnySelected = false;
  }

  @Override
  public void init() {
    super.init();

    mDisplaySensorsInteractor =
        mSensorsDisplayInteractorFactory.create(new SelectedMessageDisplayer());

    mDisplaySensorsInteractor.execute();
  }

  @Override
  public void destroy() {
    super.destroy();
    unsubscribe(mDisplaySensorsInteractor);
  }

  public String getSensorName() {
    return mSensorName;
  }

  public PointGeometryApp getSensorLocation() {
    return mSensorLocation;
  }

  public boolean isAnySelected() {
    return mIsAnySelected;
  }

  private class SelectedMessageDisplayer implements DisplaySensorsInteractor.Displayer {
    @Override
    public void display(SensorMetadata sensorMetadata) {
      //do nothing
    }

    @Override
    public void displayAsSelected(SensorMetadata sensorMetadata) {
      mSensorName = sensorMetadata.getName();

      PointGeometry geometry = (PointGeometry) sensorMetadata.getGeoEntity().getGeometry();
      mSensorLocation = PointGeometryApp.create(geometry);

      mIsAnySelected = true;
      notifyChange();
    }
  }
}

package com.teamagam.gimelgimel.app.sensor.viewModel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

public class SensorDetailViewModel extends BaseViewModel {

    private static int sCount = 0;

    private String mSensorName;
    private PointGeometryApp mSensorLocation;

    public SensorDetailViewModel() {
        mSensorLocation = PointGeometryApp.DEFAULT_POINT;
        mSensorName = "A name " + sCount++;
    }

    public String getSensorName() {
        return mSensorName;
    }

    public PointGeometryApp getSensorLocation() {
        return mSensorLocation;
    }
}

package com.teamagam.gimelgimel.app.sensor.viewModel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

public class SensorDetailsViewModel extends BaseViewModel {

    private static int sCount = 3;

    private String mSensorName;
    private PointGeometryApp mSensorLocation;

    public SensorDetailsViewModel() {
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

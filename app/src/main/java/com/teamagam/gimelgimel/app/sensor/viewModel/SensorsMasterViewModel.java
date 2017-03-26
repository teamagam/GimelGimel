package com.teamagam.gimelgimel.app.sensor.viewModel;

import android.support.v7.widget.RecyclerView;

import com.teamagam.gimelgimel.app.common.base.ViewModels.RecyclerViewModel;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.sensor.model.SensorMetadataApp;
import com.teamagam.gimelgimel.app.sensor.view.SensorsMasterFragment;
import com.teamagam.gimelgimel.app.sensor.viewModel.adapter.SensorRecyclerArrayAdapter;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.DisplaySensorsInteractor;
import com.teamagam.gimelgimel.domain.sensors.DisplaySensorsInteractorFactory;
import com.teamagam.gimelgimel.domain.sensors.SelectSensorInteractorFactory;

import javax.inject.Inject;

public class SensorsMasterViewModel extends RecyclerViewModel<SensorsMasterFragment> {

    @Inject
    DisplaySensorsInteractorFactory mSensorsFactory;

    @Inject
    SelectSensorInteractorFactory mSelectSensorFactory;

    private final SensorRecyclerArrayAdapter mRecyclerAdapter;
    private DisplaySensorsInteractor mDisplaySensorsInteractor;

    @Inject
    SensorsMasterViewModel() {
        mRecyclerAdapter = new SensorRecyclerArrayAdapter(new SensorItemClickListener());
    }

    @Override
    public void init() {
        super.init();
        mDisplaySensorsInteractor = mSensorsFactory.create(new SensorsDisplayer());

        mDisplaySensorsInteractor.execute();
    }

    @Override
    public void destroy() {
        super.destroy();
        unsubscribe(mDisplaySensorsInteractor);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return mRecyclerAdapter;
    }

    private class SensorItemClickListener
            implements BaseRecyclerArrayAdapter.OnItemClickListener<SensorMetadataApp> {
        @Override
        public void onListItemInteraction(SensorMetadataApp item) {
            mSelectSensorFactory.create(item.getId()).execute();
        }
    }

    private class SensorsDisplayer implements DisplaySensorsInteractor.Displayer {
        @Override
        public void display(SensorMetadata sensorMetadata) {
            SensorMetadataApp sma = new SensorMetadataApp(sensorMetadata.getId(),
                    sensorMetadata.getName(), sensorMetadata.getGeoEntity());
            mRecyclerAdapter.show(sma);
        }

        @Override
        public void displayAsSelected(SensorMetadata sensorMetadata) {
            mRecyclerAdapter.select(sensorMetadata.getId());
        }
    }
}

package com.teamagam.gimelgimel.app.sensor.viewModel.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerViewHolder;
import com.teamagam.gimelgimel.app.common.base.adapters.DataRandomAccessor;
import com.teamagam.gimelgimel.app.sensor.model.SensorMetadataApp;

import butterknife.BindView;

public class SensorRecyclerArrayAdapter extends
        BaseRecyclerArrayAdapter<SensorRecyclerArrayAdapter.SensorRecyclerViewHolder, SensorMetadataApp> {


    private Drawable mSensorDrawable;
    private SensorMetadataApp mLastSelected;
    private DataRandomAccessor<SensorMetadataApp> mAccessor;

    public SensorRecyclerArrayAdapter(
            DataRandomAccessor<SensorMetadataApp> data,
            OnItemClickListener<SensorMetadataApp> listener) {
        super(data, listener);
        mAccessor = data;
    }

    public synchronized void select(String sensorId) {
        unselectLast();
        selectNew(sensorId);
        notifyDataSetChanged();
    }

    @Override
    protected SensorRecyclerViewHolder createNewViewHolder(View v) {
        return new SensorRecyclerViewHolder(v);
    }

    @Override
    protected int getSingleItemLayoutRes() {
        return R.layout.recycler_sensor_list_item;
    }

    @Override
    protected void bindItemToView(SensorRecyclerViewHolder holder,
                                  SensorMetadataApp sensorMetadataApp) {
        setItemDrawable(holder);

        holder.nameTextView.setText(sensorMetadataApp.getName());

        setBackgroundColor(holder, sensorMetadataApp);
    }

    private void unselectLast() {
        if (mLastSelected != null) {
            mLastSelected.unselect();
            mLastSelected = null;
        }
    }

    private void selectNew(String sensorId) {
        int idx = mAccessor.getPosition(sensorId);
        SensorMetadataApp sensorMetadataApp = mAccessor.get(idx);
        sensorMetadataApp.select();
        mLastSelected = sensorMetadataApp;
    }

    private void setItemDrawable(SensorRecyclerViewHolder holder) {
        Drawable cameraDrawable = getSensorItemDrawable(holder);
        holder.iconImageView.setImageDrawable(cameraDrawable);
    }

    private void setBackgroundColor(SensorRecyclerViewHolder holder,
                                    SensorMetadataApp sensorMetadataApp) {
        int color = sensorMetadataApp.isSelected() ? R.color.selected_list_item : R.color.default_list_item;
        holder.setBackgroundColor(color);
    }

    private Drawable getSensorItemDrawable(SensorRecyclerViewHolder holder) {
        if (mSensorDrawable != null) {
            return mSensorDrawable;
        }

        mSensorDrawable = createSensorItemDrawable(holder);

        return mSensorDrawable;
    }

    private Drawable createSensorItemDrawable(
            SensorRecyclerViewHolder holder) {
        Context context = holder.iconImageView.getContext();
        return context.getDrawable(R.drawable.ic_dashboard);
    }

    static class SensorRecyclerViewHolder extends BaseRecyclerViewHolder<SensorMetadataApp> {

        @BindView(R.id.recycler_sensor_list_item_image)
        public ImageView iconImageView;

        @BindView(R.id.recycler_sensor_list_item_name)
        public TextView nameTextView;


        public SensorRecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}

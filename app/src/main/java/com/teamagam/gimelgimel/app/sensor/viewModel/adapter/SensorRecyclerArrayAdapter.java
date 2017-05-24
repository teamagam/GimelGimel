package com.teamagam.gimelgimel.app.sensor.viewModel.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerViewHolder;
import com.teamagam.gimelgimel.app.sensor.model.SensorMetadataApp;
import java.util.Comparator;

public class SensorRecyclerArrayAdapter
    extends BaseRecyclerArrayAdapter<SensorRecyclerArrayAdapter.ViewHolder, SensorMetadataApp> {

  private Drawable mSensorDrawable;
  private SensorMetadataApp mLastSelected;

  public SensorRecyclerArrayAdapter(OnItemClickListener<SensorMetadataApp> onItemClickListener) {
    super(SensorMetadataApp.class, new SensorComparator(), onItemClickListener);
  }

  public synchronized void select(String sensorId) {
    unselectLast();
    selectNew(sensorId);
    notifyDataSetChanged();
  }

  @Override
  protected ViewHolder createNewViewHolder(View v, int viewType) {
    return new ViewHolder(v);
  }

  @Override
  protected int getListItemLayout(int viewType) {
    return R.layout.recycler_sensor_list_item;
  }

  @Override
  protected void bindItemToView(ViewHolder holder, SensorMetadataApp sensorMetadataApp) {
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
    SensorMetadataApp sensorMetadataApp = getById(sensorId);
    sensorMetadataApp.select();
    mLastSelected = sensorMetadataApp;
  }

  private void setItemDrawable(ViewHolder holder) {
    Drawable cameraDrawable = getSensorItemDrawable(holder);
    holder.iconImageView.setImageDrawable(cameraDrawable);
  }

  private void setBackgroundColor(ViewHolder holder, SensorMetadataApp sensorMetadataApp) {
    int color =
        sensorMetadataApp.isSelected() ? R.color.selected_list_item : R.color.default_list_item;
    holder.setBackgroundColor(color);
  }

  private Drawable getSensorItemDrawable(ViewHolder holder) {
    if (mSensorDrawable != null) {
      return mSensorDrawable;
    }

    mSensorDrawable = createSensorItemDrawable(holder);

    return mSensorDrawable;
  }

  private Drawable createSensorItemDrawable(ViewHolder holder) {
    Context context = holder.iconImageView.getContext();
    return ContextCompat.getDrawable(context, R.drawable.ic_dashboard);
  }

  static class ViewHolder extends BaseRecyclerViewHolder<SensorMetadataApp> {

    @BindView(R.id.recycler_sensor_list_item_image)
    ImageView iconImageView;

    @BindView(R.id.recycler_sensor_list_item_name)
    TextView nameTextView;

    ViewHolder(View itemView) {
      super(itemView);
    }
  }

  private static class SensorComparator implements Comparator<SensorMetadataApp> {
    @Override
    public int compare(SensorMetadataApp o1, SensorMetadataApp o2) {
      return o1.getName().compareTo(o2.getName());
    }
  }
}

package com.teamagam.gimelgimel.app.sensor.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerViewHolder;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.sensor.model.SensorMetadataApp;
import com.teamagam.gimelgimel.app.sensor.viewModel.SensorsMasterViewModel;
import com.teamagam.gimelgimel.databinding.FragmentSensorsMasterBinding;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;

import butterknife.BindView;


public class SensorsMasterFragment extends BaseDataFragment<SensorsMasterViewModel> {

    private static int sCount = 0;
    private static SensorMetadataApp[] sSensorMetaDatas = new SensorMetadataApp[]{
            generate(), generate(), generate()
    };

    private static SensorMetadataApp generate() {
        sCount++;
        return new SensorMetadataApp("sensorId" + sCount, "sensorName" + sCount,
                new SensorEntity("sensorId" + sCount, "text",
                        new PointGeometry(34, 32)));
    }

    private SensorsMasterViewModel mViewModel;
    private RecyclerView mRecyclerView;

    public SensorsMasterFragment() {
        mViewModel = new SensorsMasterViewModel();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_sensors_master;
    }

    @Override
    protected SensorsMasterViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentSensorsMasterBinding binding = FragmentSensorsMasterBinding.bind(rootView);
        binding.setViewModel(mViewModel);
        return binding;
    }

    @Override
    protected void createSpecificViews(View rootView) {
        super.createSpecificViews(rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_sensor_recycler);
        mRecyclerView.setAdapter(new RecyclerView.Adapter<SensorItemViewHolder>() {

            @Override
            public SensorItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recycler_sensor_list_item, parent, false);
                return new SensorItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(SensorItemViewHolder holder, int position) {

                Context context = holder.iconImageView.getContext();
                Drawable cameraDrawable = context.getDrawable(R.drawable.ic_dashboard);
                holder.iconImageView.setImageDrawable(cameraDrawable);
                SensorMetadataApp sensorMetadataApp = sSensorMetaDatas[position];
                holder.nameTextView.setText(sensorMetadataApp.getName());
            }


            @Override
            public int getItemCount() {
                return 3;
            }
        });
    }


    static class SensorItemViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.recycler_sensor_list_item_image)
        public ImageView iconImageView;

        @BindView(R.id.recycler_sensor_list_item_name)
        public TextView nameTextView;

        public SensorItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}

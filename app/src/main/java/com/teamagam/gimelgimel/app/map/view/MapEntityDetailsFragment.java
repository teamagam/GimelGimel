package com.teamagam.gimelgimel.app.map.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.map.viewModel.MapEntityDetailsViewModel;
import com.teamagam.gimelgimel.databinding.FragmentMapEntityDetailsBinding;

import javax.inject.Inject;


public class MapEntityDetailsFragment extends BaseDataFragment<MapEntityDetailsViewModel> {

    @Inject
    MapEntityDetailsViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map_entity_details;
    }

    @Override
    protected MapEntityDetailsViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentMapEntityDetailsBinding bind =
                com.teamagam.gimelgimel.databinding.FragmentMapEntityDetailsBinding.bind(rootView);
        bind.setViewModel(mViewModel);
        return bind;
    }
}

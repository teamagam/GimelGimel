package com.teamagam.gimelgimel.app.map.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.map.viewModel.MapEntityDetailsViewModel;
import com.teamagam.gimelgimel.databinding.FragmentMapEntityDetailsBinding;

import javax.inject.Inject;

import butterknife.BindView;


public class MapEntityDetailsFragment extends BaseDataFragment<MapEntityDetailsViewModel> {

    public static final String NAME_KEY = "name";
    public static final String DESCRIPTION_KEY = "description";

    @BindView(R.id.map_entity_name)
    TextView mEntityName;

    @BindView(R.id.map_entity_description)
    TextView mEntityDescription;

    @Inject
    MapEntityDetailsViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mEntityName.setText(args.getString(NAME_KEY));
            mEntityDescription.setText(args.getString(DESCRIPTION_KEY));
        }
        return view;
    }

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

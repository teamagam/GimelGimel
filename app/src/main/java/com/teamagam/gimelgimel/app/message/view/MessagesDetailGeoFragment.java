package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.viewModel.GeoMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.viewModel.GeoMessageDetailViewModelFactory;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.databinding.FragmentMessageDetailGeoBinding;

import javax.inject.Inject;

/**
 * A subclass {@link MessagesDetailFragment} for showing Geo Messages.
 */
public class MessagesDetailGeoFragment extends MessagesDetailFragment<GeoMessageDetailViewModel> {

    @Inject
    GeoMessageDetailViewModelFactory mViewModelFactory;
    private GeoMessageDetailViewModel mViewModel;

    public MessagesDetailGeoFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);

        mViewModel = mViewModelFactory.create();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_detail_geo;
    }

    @Override
    protected GeoMessageDetailViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentMessageDetailGeoBinding bind = FragmentMessageDetailGeoBinding.bind(rootView);
        bind.setViewModel(mViewModel);
        mViewModel.setView(this);
        return bind;
    }
}


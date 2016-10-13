package com.teamagam.gimelgimel.app.message.view;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.viewModel.GeoMessageDetailViewModel;

import javax.inject.Inject;

/**
 * A subclass {@link MessagesDetailFragment} for showing Geo Messages.
 */
public class MessagesDetailGeoFragment extends MessagesDetailFragment<GeoMessageDetailViewModel> {

    @Inject
    GeoMessageDetailViewModel mViewModel;

    public MessagesDetailGeoFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_detail_geo;
    }

    @Override
    protected GeoMessageDetailViewModel getSpecificViewModel() {
        return mViewModel;
    }

}


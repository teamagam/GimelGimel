package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.LatLongMessageDetailViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A subclass {@link MessagesDetailBaseGeoFragment} for showing Geo Messages.
 */
public class MessagesDetailGeoFragment extends MessagesDetailBaseGeoFragment<LatLongMessageDetailViewModel> {

    @BindView(R.id.fragment_messages_detail_lat_textview)
    TextView mLatTV;

    @BindView(R.id.fragment_messages_detail_lon_textview)
    TextView mLonTV;

    @BindView(R.id.fragment_messages_detail_goto_button)
    Button mGotoBtn;

    public MessagesDetailGeoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_detail_geo;
    }

    @Override
    void getSpecificViewModel() {
        mMessageViewModel = mApp.getLatLongMessageDetailViewModel();
    }

    @OnClick(R.id.fragment_messages_detail_goto_button)
    public void gotoLocationClicked() {
        gotoLocationClicked(mMessageViewModel.getPointGeometry());
    }

    @Override
    protected void updateContentViews() {
        mLatTV.setText(String.valueOf(mMessageViewModel.getPointGeometry().latitude));
        mLonTV.setText(String.valueOf(mMessageViewModel.getPointGeometry().longitude));
    }

}


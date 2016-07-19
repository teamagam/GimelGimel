package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.GeoMessageDetailViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A subclass {@link MessagesDetailBaseGeoFragment} for showing Geo Messages.
 */
public class MessagesDetailGeoFragment extends MessagesDetailBaseGeoFragment<GeoMessageDetailViewModel> {

    @BindView(R.id.fragment_messages_detail_lat_textview)
    TextView mLatTV;

    @BindView(R.id.fragment_messages_detail_lon_textview)
    TextView mLonTV;

    @BindView(R.id.fragment_messages_detail_text_textview)
    TextView mTextTV;

    @BindView(R.id.fragment_messages_detail_locationtype_textview)
    Button mLoactionTextTV;

    @BindView(R.id.fragment_messages_detail_goto_button)
    Button mGotoBtn;

    @BindView(R.id.fragment_messages_detail_show_pin_button)
    Button mShowPinBtn;

    public MessagesDetailGeoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_detail_geo;
    }

    @Override
    protected void getSpecificViewModel() {
        mViewModel = mApp.getLatLongMessageDetailViewModel();
    }

    @OnClick(R.id.fragment_messages_detail_goto_button)
    public void gotoLocationClicked() {
        gotoLocationClicked(mViewModel.getPointGeometry());
    }

    @OnClick(R.id.fragment_messages_detail_show_pin_button)
    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    @Override
    protected void updateContentViews() {
        mLatTV.setText(String.valueOf(mViewModel.getPointGeometry().latitude));
        mLonTV.setText(String.valueOf(mViewModel.getPointGeometry().longitude));
        mTextTV.setText(String.valueOf(mViewModel.getText()));
        mLoactionTextTV.setText(String.valueOf(mViewModel.getLocationType()));
    }

}


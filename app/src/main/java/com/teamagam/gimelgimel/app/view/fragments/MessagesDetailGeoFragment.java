package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.LatLongMessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesDetailGeoFragment extends MessagesDetailFragment {

    @BindView(R.id.fragment_messages_detail_lat_textview)
    TextView mLatTV;

    @BindView(R.id.fragment_messages_detail_lon_textview)
    TextView mLonTV;

    @BindView(R.id.fragment_messages_detail_goto_button)
    Button mGotoBtn;

    private LatLongMessageDetailViewModel mMessageViewModel;
    private GeoMessageFragmentInterface mListener;

    public MessagesDetailGeoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GeoMessageFragmentInterface) {
            mListener = (GeoMessageFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_detail_geo;
    }

    @Override
    void getSpecificViewModel() {
        mMessageViewModel = mApp.getLatLongMessageDetailViewModel();
        mMessageViewModel.addObserver(this);
    }

    @OnClick(R.id.fragment_messages_detail_goto_button)
    public void gotoLocationClicked() {
        sLogger.userInteraction("goto button clicked");
        mListener.goToLocation(mMessageViewModel.getPointGeometry());
    }

    @Override
    protected void updateViews() {
        updateTitle(mMessageViewModel.getSenderId(), mMessageViewModel.getDate());
        mLatTV.setText(String.valueOf(mMessageViewModel.getPointGeometry().latitude));
        mLonTV.setText(String.valueOf(mMessageViewModel.getPointGeometry().longitude));
    }

    /**
     * Containing activity or target fragment must implement this interface!
     * It is essential for this fragment communication
     */
    public interface GeoMessageFragmentInterface {
        void goToLocation(PointGeometry pointGeometry);
    }

}


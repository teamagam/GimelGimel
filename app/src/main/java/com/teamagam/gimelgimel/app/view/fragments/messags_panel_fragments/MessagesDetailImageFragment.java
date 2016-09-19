package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.ImageMessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.ImageFullscreenActivity;
import com.teamagam.gimelgimel.app.view.drawable.CircleProgressBarDrawable;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A subclass {@link MessagesDetailBaseGeoFragment} for showing Image Messages.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesDetailImageFragment extends MessagesDetailBaseGeoFragment<ImageMessageDetailViewModel>{

    @BindView(R.id.fragment_messages_detail_image_location)
    TextView mLocationTV;

    @BindView(R.id.image_source_type)
    TextView mSourceTV;

    @BindView(R.id.fragment_image_goto_button)
    Button mGoToBtn;

    @BindView(R.id.fragment_image_show_pin_button)
    Button mShowPinBtn;

    @BindView(R.id.image_view)
    SimpleDraweeView mDraweeView;

    private Uri mUri;

    public MessagesDetailImageFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_image;
    }

    @Override
    protected void createSpecificViews(View rootView) {
        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
    }

    @Override
    protected void getSpecificViewModel() {
        mViewModel = mApp.getImageMessageDetailViewModel();
    }

    @OnClick(R.id.fragment_image_goto_button)
    public void gotoImageClicked() {
        gotoLocationClicked(mViewModel.getPointGeometry());
    }

    @OnClick(R.id.fragment_image_show_pin_button)
    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    @OnClick(R.id.image_view)
    public void expandViewClicked() {
        sLogger.userInteraction("expand view clicked");

        Intent fullscreenIntent = new Intent(getActivity(), ImageFullscreenActivity.class);
        fullscreenIntent.setData(mUri);
        startActivity(fullscreenIntent);
    }

    @Override
    protected void updateContentViews() {
        mSourceTV.setText(mViewModel.getImageSource());

        mUri = Uri.parse(mViewModel.getImageUrl());
        mDraweeView.setImageURI(mUri);

        if (mViewModel.hasLocation() && mViewModel.getPointGeometry() != null) {
            PointGeometry point = mViewModel.getPointGeometry();
            mLocationTV.setText(
                    getString(R.string.geo_dd_format, point.latitude, point.longitude));
        } else {
            mLocationTV.setText(R.string.fragment_messages_image_location_unavailable);
        }

        mGoToBtn.setEnabled(mViewModel.hasLocation());
        mShowPinBtn.setEnabled(mViewModel.hasLocation());

        //todo: add image date
    }
}


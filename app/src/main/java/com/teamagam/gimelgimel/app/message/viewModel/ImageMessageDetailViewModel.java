package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailImageFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;

import java.util.Date;

/**
 * Image message details view-model
 */
public class ImageMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailImageFragment> {

    public ImageMessageDetailViewModel() {
        super();
    }

    public String getImageUrl() {
        return getSelectedImageMetaData().getURL();
    }

    public Date getImageDate() {
        return new Date(getSelectedImageMetaData().getTime());
    }

    public boolean hasLocation() {
        return getSelectedImageMetaData().hasLocation();
    }

    public PointGeometry getPointGeometry() {
        return getSelectedImageMetaData().getLocation();
    }

    @ImageMetadata.SourceType
    public String getImageSource() {
        return getSelectedImageMetaData().getSource();
    }

    public void gotoImageClicked() {
        super.gotoLocationClicked(getPointGeometry());
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    @Override
    protected String getExpectedMessageType() {
        return Message.IMAGE;
    }

    private ImageMetadata getSelectedImageMetaData() {
        return (ImageMetadata) mMessageSelected.getContent();
    }

    public void expandViewClicked() {
        sLogger.userInteraction("expand view clicked");

//        Intent fullscreenIntent = new Intent(getActivity(), ImageFullscreenActivity.class);
//        fullscreenIntent.setData(mUri);
//        startActivity(fullscreenIntent);
    }

    protected void updateContentViews() {
//        mSourceTV.setText(mViewModel.getImageSource());
//
//        mUri = Uri.parse(mViewModel.getImageUrl());
//        mDraweeView.setImageURI(mUri);
//
//        if (mViewModel.hasLocation() && mViewModel.getPointGeometry() != null) {
//            PointGeometry point = mViewModel.getPointGeometry();
//            mLocationTV.setText(
//                    getString(R.string.geo_dd_format, point.latitude, point.longitude));
//        } else {
//            mLocationTV.setText(R.string.fragment_messages_image_location_unavailable);
//        }
//
//        mGoToBtn.setEnabled(mViewModel.hasLocation());
//        mShowPinBtn.setEnabled(mViewModel.hasLocation());

        //todo: add image date
    }
}

package com.teamagam.gimelgimel.app.message.viewModel;

import android.app.Activity;
import android.net.Uri;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailImageFragment;
import com.teamagam.gimelgimel.app.view.Navigator;

import java.util.Date;

import javax.inject.Inject;

/**
 * Image message details view-model
 */
public class ImageMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailImageFragment> {

    @Inject
    Navigator mNavigator;

    @Inject
    Activity mActivity;

    @Inject
    public ImageMessageDetailViewModel() {
        super();
    }

    public Uri getImageUri() {
        return Uri.parse(getSelectedImageMetaData().getURL());
    }

    public Date getImageDate() {
        return new Date(getSelectedImageMetaData().getTime());
    }

    public boolean hasLocation() {
        return getSelectedImageMetaData().hasLocation();
    }

    public PointGeometryApp getPointGeometry() {
        return getSelectedImageMetaData().getLocation();
    }

//    @ImageMetadataApp.SourceType
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
        return MessageApp.IMAGE;
    }

    private ImageMetadataApp getSelectedImageMetaData() {
        return (ImageMetadataApp) mMessageSelected.getContent();
    }

    public void expandViewClicked() {
        sLogger.userInteraction("expand view clicked");
        mNavigator.navigateToFullScreenImage(mActivity, getImageUri());
    }

}

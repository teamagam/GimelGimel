package com.teamagam.gimelgimel.app.message.viewModel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailImageFragment;
import com.teamagam.gimelgimel.app.view.Navigator;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;

import java.util.Date;

/**
 * Image message details view-model
 */
@AutoFactory
public class ImageMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailImageFragment> {

    private final Navigator mNavigator;
    private final Activity mActivity;
    private final MessageImageApp mImageMessage;
    private final GeoEntityTransformer mTransformer;

    public ImageMessageDetailViewModel(
            @Provided Context context,
            @Provided GoToLocationMapInteractorFactory gotoFactory,
            @Provided DrawMessageOnMapInteractorFactory drawFactory,
            @Provided Navigator navigator,
            @Provided Activity activity,
            @Provided GeoEntityTransformer transformer,
            MessageImageApp messageApp) {
        super(context, gotoFactory, drawFactory, messageApp);
        mNavigator = navigator;
        mActivity = activity;
        mTransformer = transformer;
        mImageMessage = messageApp;
    }


    public Uri getImageUri() {
        return Uri.parse(getImageMetadata().getURL());
    }

    public Date getImageDate() {
        return new Date(getImageMetadata().getTime());
    }

    public boolean hasLocation() {
        return getImageMetadata().hasLocation();
    }

    public PointGeometryApp getPointGeometry() {
        return (PointGeometryApp) mTransformer.transform(
                mImageMessage.getContent().getGeoEntity()).getGeometry();
    }

    public String getImageSource() {
        return getImageMetadata().getSource();
    }

    public void gotoImageClicked() {
        super.gotoLocationClicked(getPointGeometry());
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    public void expandViewClicked() {
        sLogger.userInteraction("expand view clicked");
        mNavigator.navigateToFullScreenImage(mActivity, getImageUri());
    }

    private ImageMetadataApp getImageMetadata() {
        return mImageMessage.getContent();
    }
}

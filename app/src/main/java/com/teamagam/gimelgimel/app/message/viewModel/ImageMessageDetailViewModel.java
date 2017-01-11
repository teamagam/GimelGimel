package com.teamagam.gimelgimel.app.message.viewModel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.app.message.view.MessagesDetailImageFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.Date;

import javax.inject.Inject;

/**
 * Image message details view-model
 */
public class ImageMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailImageFragment> {

    private Activity mActivity;
    private MessageAppMapper mMessageAppMapper;
    private MessageImageApp mMessage;
    private GeoEntityTransformer mGeoEntityTransformer;

    @Inject
    public ImageMessageDetailViewModel(
            Context context,
            DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory,
            GoToLocationMapInteractorFactory gotoFactory,
            DrawMessageOnMapInteractorFactory drawFactory,
            Activity activity,
            GeoEntityTransformer transformer,
            MessageAppMapper messageAppMapper) {
        super(context, selectedMessageInteractorFactory, gotoFactory, drawFactory);
        mActivity = activity;
        mGeoEntityTransformer = transformer;
        mMessageAppMapper = messageAppMapper;
    }

    public Uri getImageUri() {
        ImageMetadataApp imageMetadata = getImageMetadata();

        if (imageMetadata != null) {
            return Uri.parse(imageMetadata.getURL());
        }

        return null;
    }

    public Date getImageDate() {
        return new Date(getImageMetadata().getTime());
    }

    public boolean hasLocation() {
        ImageMetadataApp imageMetadata = getImageMetadata();

        return imageMetadata != null && imageMetadata.hasLocation();
    }

    public PointGeometryApp getPointGeometry() {
        if (mMessage != null) {
            return (PointGeometryApp) mGeoEntityTransformer.transform(
                    mMessage.getContent().getGeoEntity()).getGeometry();
        }

        return null;
    }

    public String getImageSource() {
        ImageMetadataApp imageMetadata = getImageMetadata();

        if (imageMetadata != null) {
            return getImageMetadata().getSource();
        }

        return null;
    }

    public void gotoImageClicked() {
        super.gotoLocationClicked(getPointGeometry());
    }

    public void showPinOnMapClicked() {
        super.showPinOnMapClicked();
    }

    public void expandViewClicked() {
        sLogger.userInteraction("expand view clicked");
        Navigator.navigateToFullScreenImage(mActivity, getImageUri());
    }

    private ImageMetadataApp getImageMetadata() {
        if (mMessage != null) {
            return mMessage.getContent();
        }

        return null;
    }

    @Override
    protected MessageApp getMessage() {
        return mMessage;
    }

    @Override
    protected DisplaySelectedMessageInteractor.Displayer createDisplayer() {
        return new DisplaySelectedMessageInteractor.Displayer() {
            @Override
            public void display(Message message) {
                mMessage = (MessageImageApp) mMessageAppMapper.transformToModel(message);

                notifyChange();
            }
        };
    }
}

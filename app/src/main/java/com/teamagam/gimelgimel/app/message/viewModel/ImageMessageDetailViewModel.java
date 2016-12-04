package com.teamagam.gimelgimel.app.message.viewModel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
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

/**
 * Image message details view-model
 */
@AutoFactory
public class ImageMessageDetailViewModel extends MessageBaseGeoViewModel<MessagesDetailImageFragment> {

    private Navigator mNavigator;
    private Activity mActivity;
    private MessageAppMapper mMessageAppMapper;
    private MessageImageApp mImageMessage;
    private GeoEntityTransformer mGeoEntityTransformer;

    public ImageMessageDetailViewModel(
            @Provided Context context,
            @Provided DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory,
            @Provided GoToLocationMapInteractorFactory gotoFactory,
            @Provided DrawMessageOnMapInteractorFactory drawFactory,
            @Provided Navigator navigator,
            @Provided Activity activity,
            @Provided GeoEntityTransformer transformer,
            @Provided MessageAppMapper messageAppMapper) {
        super(context, selectedMessageInteractorFactory, gotoFactory, drawFactory);
        mNavigator = navigator;
        mActivity = activity;
        mGeoEntityTransformer = transformer;
        mMessageAppMapper = messageAppMapper;
    }

    @Override
    public void start() {
        super.start();

        createInteractor();
        mDisplaySelectedMessageInteractor.execute();
    }

    public Uri getImageUri() {
        ImageMetadataApp imageMetadata = getImageMetadata();

        if(imageMetadata != null) {
            return Uri.parse(imageMetadata.getURL());
        }

        return  null;
    }

    public Date getImageDate() {
        return new Date(getImageMetadata().getTime());
    }

    public boolean hasLocation() {
        ImageMetadataApp imageMetadata = getImageMetadata();

        return imageMetadata != null && imageMetadata.hasLocation();
    }

    public PointGeometryApp getPointGeometry() {
        if(mImageMessage != null) {
            return (PointGeometryApp) mGeoEntityTransformer.transform(
                    mImageMessage.getContent().getGeoEntity()).getGeometry();
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
        mNavigator.navigateToFullScreenImage(mActivity, getImageUri());
    }

    private ImageMetadataApp getImageMetadata() {
        if(mImageMessage != null) {
            return mImageMessage.getContent();
        }

        return null;
    }

    private void createInteractor() {
        mDisplaySelectedMessageInteractor = mDisplaySelectedMessageInteractorFactory.create(
                new DisplaySelectedMessageInteractor.Displayer() {
                    @Override
                    public void display(Message message) {
                        mMessage = mMessageAppMapper.transformToModel(message);
                        mImageMessage = (MessageImageApp) mMessage;

                        notifyChange();
                    }
                });
    }
}

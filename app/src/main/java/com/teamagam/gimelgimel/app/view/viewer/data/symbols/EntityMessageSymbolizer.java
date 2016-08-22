package com.teamagam.gimelgimel.app.view.viewer.data.symbols;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.IMessageVisitor;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageGeo;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * message Symbolizer that uses visitor pattern.
 */
@PerActivity
public class EntityMessageSymbolizer implements
        IMessageSymbolizer {

    private Symbol mSymbolResult;
    private final Map<String, String> mEntityTypeToMarkerUrl;
    private String mImageMarkerUrl;
    private MessageSymbolizeVisitor mSymbolizeVisitor;

    @Inject
    public EntityMessageSymbolizer(Context context) {
        mSymbolizeVisitor = new MessageSymbolizeVisitor();
        mEntityTypeToMarkerUrl = new HashMap<>();
        initEntityMarkersMap(context);
        initImageMarkerPath(context);
    }

    void initImageMarkerPath(Context context) {
        mImageMarkerUrl = context.getString(R.string.geo_locations_marker_image);
    }

    private void initEntityMarkersMap(Context context) {
        String[] entityTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        String[] entityMarkersUrl = context.getResources().getStringArray(R.array.geo_locations_markers_matched_types);

        for (int i = 0; i < entityTypes.length; i++) {
            mEntityTypeToMarkerUrl.put(entityTypes[i], entityMarkersUrl[i]);
        }
    }

    @Override
    public Symbol symbolize(Message message) {
        message.accept(mSymbolizeVisitor);
        return mSymbolResult;
    }

    private class MessageSymbolizeVisitor implements IMessageVisitor {

        @Override
        public void visit(MessageGeo message) {
            String symbolPath = mEntityTypeToMarkerUrl.get(message.getContent().getType());
            createImageSymbolFromPath(symbolPath);
        }

        @Override
        public void visit(MessageImage message) {
            createImageSymbolFromPath(mImageMarkerUrl);
        }

        @Override
        public void visit(MessageUserLocation message) {
            if (isStale(message.getContent().getAgeMillis())) {
                mSymbolResult = createActiveUserLocationSymbol(message.getSenderId());
            } else {
                mSymbolResult = createStaleUserLocationSymbol(message.getSenderId());
            }
        }

        @Override
        public void visit(MessageText message) {
            //empty
            throw new IllegalArgumentException("Message type \"text\" symbol is not supported");
        }

        private void createImageSymbolFromPath(String mImageMarkerPath) {
            mSymbolResult = new PointImageSymbol(
                    mImageMarkerPath,
                    36, 36);
        }

        private boolean isStale(long userLocationAgeMillis) {
            return userLocationAgeMillis < Constants.USER_LOCATION_STALE_THRESHOLD_MS;
        }

        private Symbol createActiveUserLocationSymbol(String userId) {
            return new PointTextSymbol(Constants.ACTIVE_USER_LOCATION_PIN_CSS_COLOR,
                    userId, Constants.USER_LOCATION_PIN_SIZE_PX);
        }

        private Symbol createStaleUserLocationSymbol(String userId) {
            return new PointTextSymbol(Constants.STALE_USER_LOCATION_PIN_CSS_COLOR,
                    userId,
                    Constants.USER_LOCATION_PIN_SIZE_PX);
        }
    }

}

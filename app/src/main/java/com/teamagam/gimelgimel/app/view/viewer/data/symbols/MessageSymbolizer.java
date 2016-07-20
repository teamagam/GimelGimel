package com.teamagam.gimelgimel.app.view.viewer.data.symbols;

import com.teamagam.gimelgimel.app.model.ViewsModels.IMessageVisitor;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageGeo;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.view.fragments.ViewerFragment;

/**
 * message Symbolizer that uses visitor pattern.
 */
public class MessageSymbolizer implements
        IMessageVisitor,
        ViewerFragment.MessageSymbolizer {

    Symbol mSymbolResult;

    @Override
    public Symbol symbolize(Message message) {
        message.accept(this);
        return mSymbolResult;
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
    public void visit(MessageGeo message) {

    }

    @Override
    public void visit(MessageText message) {

    }

    @Override
    public void visit(MessageImage message) {

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

package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.message.model.MessageGeoModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * LatLong message view-model
 */
public class GeoMessageDetailViewModel extends MessageDetailViewModel {
    public GeoMessageDetailViewModel(
            SelectedMessageModel selectedMessageModel) {
        super(selectedMessageModel);
    }

    public PointGeometry getPointGeometry() {
        validateSelectedMessage();
        return getSelectedMessagePointGeometry();
    }

    @Override
    protected String getExpectedMessageType() {
        return Message.GEO;
    }

    private PointGeometry getSelectedMessagePointGeometry() {
        return ((MessageGeoModel) mSelectedMessageModel.getSelected()).getContent().getPointGeometry();
    }

    public String getText() {
        return ((MessageGeoModel) mSelectedMessageModel.getSelected()).getContent().getText();
    }

    public String getLocationType() {
        return ((MessageGeoModel) mSelectedMessageModel.getSelected()).getContent().getType();
    }
}

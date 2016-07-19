package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

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
        return (PointGeometry) mSelectedMessageModel.getSelected().getContent();
    }
}

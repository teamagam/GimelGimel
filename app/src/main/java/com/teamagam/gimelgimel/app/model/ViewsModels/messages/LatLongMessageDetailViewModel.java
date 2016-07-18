package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * LatLong message view-model
 */
public class LatLongMessageDetailViewModel extends MessageDetailViewModel {
    public LatLongMessageDetailViewModel(
            SelectedMessageModel selectedMessageModel) {
        super(selectedMessageModel);
    }

    public PointGeometry getPointGeometry() {
        validateSelectedMessage();
        return getSelectedMessagePointGeometry();
    }

    @Override
    protected String getExpectedMessageType() {
        return Message.LAT_LONG;
    }

    private PointGeometry getSelectedMessagePointGeometry() {
        return (PointGeometry) mSelectedMessageModel.getSelected().getContent();
    }
}

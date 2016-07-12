package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Date;

/**
 * Image message details view-model
 */
public class ImageMessageDetailViewModel extends MessageDetailViewModel {
    public ImageMessageDetailViewModel(
            SelectedMessageModel selectedMessageModel) {
        super(selectedMessageModel);
    }

    public String getImageUrl() {
        validateSelectedMessage();
        return getSelectedImageMetaData().getURL();
    }

    public Date getImageDate() {
        validateSelectedMessage();
        return new Date(getSelectedImageMetaData().getTime());
    }

    public boolean hasLocation() {
        validateSelectedMessage();
        return getSelectedImageMetaData().hasLocation();
    }

    public PointGeometry getPointGeometry() {
        validateSelectedMessage();
        return getSelectedImageMetaData().getLocation();
    }

    @Override
    protected String getExpectedMessageType() {
        return Message.IMAGE;
    }

    private ImageMetadata getSelectedImageMetaData() {
        return (ImageMetadata) mSelectedMessageModel.getSelected().getContent();
    }
}

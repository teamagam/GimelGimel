package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObservable;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments.MessagesDetailBaseGeoFragment;

/**
 * Shared functionality of detail message view-model
 * Verifies selected message is of appropriate type before each method call.
 * Notifies observers on data changes only when selected message is of appropriate type.
 */
public abstract class MessageDetailViewModel extends SelectedMessageViewModel implements DataChangedObservable {

    public MessageDetailViewModel(SelectedMessageModel selectedMessageModel) {
        super(selectedMessageModel);
    }

    @Override
    protected void validateSelectedMessage() {
        super.validateSelectedMessage();
        if (!isSelectedMessageOfType(getExpectedMessageType())) {
            throw new IncompatibleMessageType();
        }
    }

    @Message.MessageType
    protected abstract String getExpectedMessageType();

    @Override
    protected boolean shouldNotifyOnSelectedMessageModelChange() {
        return isSelectedMessageOfType(getExpectedMessageType());
    }

    private boolean isSelectedMessageOfType(@Message.MessageType String messageType) {
        return mSelectedMessageModel.getSelected().getType().equals(messageType);
    }

    public void drawMessageOnMap(MessagesDetailBaseGeoFragment.GeoMessageInterface drawMessageOnMapInterface) {
        drawMessageOnMapInterface.addMessageLocationPin(mSelectedMessageModel.getSelected());
    }

    static class IncompatibleMessageType extends RuntimeException {
    }
}

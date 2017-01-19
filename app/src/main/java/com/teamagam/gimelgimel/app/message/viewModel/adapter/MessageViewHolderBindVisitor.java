package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;
import com.teamagam.gimelgimel.app.sensor.model.MessageSensorApp;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ToggleMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;

import java.text.SimpleDateFormat;

public class MessageViewHolderBindVisitor implements IMessageAppVisitor {

    private MessagesRecyclerViewAdapter.MessageViewHolder mMessageViewHolder;
    private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private final ToggleMessageOnMapInteractorFactory mToggleMessageOnMapInteractorFactory;

    public MessageViewHolderBindVisitor(
            MessagesRecyclerViewAdapter.MessageViewHolder messageViewHolder,
            GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
            ToggleMessageOnMapInteractorFactory toggleMessageOnMapInteractorFactory) {
        mMessageViewHolder = messageViewHolder;
        mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
        mToggleMessageOnMapInteractorFactory = toggleMessageOnMapInteractorFactory;
    }

    @Override
    public void visit(MessageGeoApp message) {
        setMessageBase(message);
        setTextContent(message);
        setImageViewVisibility(View.GONE);
        setGeoPanelVisibility(View.VISIBLE);
        bindGeoPanel(message.getContent().getGeoEntity().getGeometry(), message.getMessageId());
        updateDisplayToggle(message);
    }

    @Override
    public void visit(MessageTextApp message) {
        setMessageBase(message);
        setTextContent(message);
        setGeoPanelVisibility(View.GONE);
        setImageViewVisibility(View.GONE);
    }

    @Override
    public void visit(MessageImageApp message) {
        setMessageBase(message);
        setImageViewVisibility(View.VISIBLE);
        setGeoPanelVisibility(View.VISIBLE);
        bindGeoPanel(message.getContent().getGeoEntity().getGeometry(), message.getMessageId());
        updateDisplayToggle(message);
    }

    @Override
    public void visit(MessageSensorApp message) {
        throw new RuntimeException("Sensor messages should not be binded to whatsapp messages");
    }

    private void setMessageBase(MessageApp message) {
        setSenderName(message);
        setDate(message);
    }

    private void setSenderName(MessageApp message) {
        mMessageViewHolder.senderTV.setText(message.getSenderId());
    }

    private void setDate(MessageApp displayMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                mMessageViewHolder.mAppContext.getString(R.string.message_list_item_time));
        mMessageViewHolder.timeTV.setText(sdf.format(displayMessage.getCreatedAt()));
    }

    private void setTextContent(MessageGeoApp messageGeoApp) {
        setTextContent(messageGeoApp.getContent().getGeoEntity().getText());
    }

    private void setTextContent(MessageTextApp message) {
        setTextContent(message.getContent());
    }

    private void setTextContent(String text) {
        mMessageViewHolder.contentTV.setText(text);
    }

    private void setImageViewVisibility(int visibility) {
        mMessageViewHolder.imageView.setVisibility(visibility);
    }

    private void setGeoPanelVisibility(int visibility) {
        mMessageViewHolder.messageGeoPanel.setVisibility(visibility);
        mMessageViewHolder.messageGeoPanelSeparator.setVisibility(visibility);
    }

    private void bindGeoPanel(Geometry geometry, String messageId) {
        bindGoto(geometry);
        bindDisplayToggle(messageId);
    }

    private void bindDisplayToggle(final String messageId) {
        mMessageViewHolder.displayToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleMessageOnMapInteractorFactory.create(messageId).execute();
            }
        });
    }

    private void bindGoto(final Geometry geometry) {
        mMessageViewHolder.gotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoToLocationMapInteractorFactory.create(geometry).execute();
            }
        });
    }

    private void updateDisplayToggle(MessageApp message) {
        mMessageViewHolder.displayToggleButton.setChecked(message.isShownOnMap());
    }
}

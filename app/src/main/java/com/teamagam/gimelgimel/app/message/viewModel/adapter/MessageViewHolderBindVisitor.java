package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.net.Uri;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
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
        setMessageDetails(message);
        setContent(message);
        setImageViewVisibility(View.GONE);
        setGeoPanel(message, View.VISIBLE, message.getContent().getGeoEntity().getGeometry());
    }

    @Override
    public void visit(MessageTextApp message) {
        setMessageDetails(message);
        setContent(message);
        setGeoPanelVisibility(View.GONE);
        setImageViewVisibility(View.GONE);
    }

    @Override
    public void visit(MessageImageApp message) {
        setMessageDetails(message);
        setImageViewVisibility(View.VISIBLE);
        setGeoPanel(message, View.VISIBLE, message.getContent().getGeoEntity().getGeometry());
        setContent(message);
        bindImageClick(message);
    }

    @Override
    public void visit(MessageSensorApp message) {
        throw new RuntimeException("Sensor messages should not be binded to whatsapp messages");
    }

    private void setMessageDetails(MessageApp message) {
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

    private void setGeoPanel(MessageApp message, int visibleFlag, Geometry geometry) {
        setGeoPanelVisibility(visibleFlag);
        bindGeoPanel(geometry, message.getMessageId());
        updateDisplayToggle(message);
    }

    private void setContent(MessageGeoApp messageGeoApp) {
        setTextContent(messageGeoApp.getContent().getGeoEntity().getText());
    }

    private void setContent(MessageTextApp message) {
        setTextContent(message.getContent());
    }

    private void setContent(MessageImageApp message) {
        setImageUrl(message);
        String sourceMessage = "Source: " + message.getContent().getSource();
        setTextContent(sourceMessage);
    }

    private void setImageUrl(MessageImageApp message) {
        Uri imageURI = getImageURI(message);
        mMessageViewHolder.imageView.setImageURI(imageURI);
    }

    private Uri getImageURI(MessageImageApp message) {
        String url = message.getContent().getURL();
        return Uri.parse(url);
    }

    private void setTextContent(String text) {
        mMessageViewHolder.contentTV.setText(text);
    }

    private void setImageViewVisibility(int visibility) {
        mMessageViewHolder.imageView.setVisibility(visibility);
    }

    private void setGeoPanelVisibility(int visibility) {
        mMessageViewHolder.messageGeoPanel.setVisibility(visibility);
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

    private void bindImageClick(final MessageImageApp message) {
        mMessageViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.navigateToFullScreenImage(v.getContext(), getImageURI(message));
            }
        });
    }
}

package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.net.Uri;
import android.view.View;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;
import com.teamagam.gimelgimel.domain.alerts.entity.VectorLayerAlert;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ToggleMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.messages.MessagePresentation;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeoImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageSensor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.MessageVectorLayer;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import java.text.SimpleDateFormat;

public class MessageViewHolderBindVisitor implements IMessageVisitor {

  private static final String STRING_EMPTY = "";

  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final ToggleMessageOnMapInteractorFactory mToggleMessageOnMapInteractorFactory;
  private final Navigator mNavigator;
  private final MessagesRecyclerViewAdapter.MessageViewHolder mMessageViewHolder;
  private final GlideLoader mGliderLoader;
  private final MessagePresentation mPresentation;

  public MessageViewHolderBindVisitor(
      MessagesRecyclerViewAdapter.MessageViewHolder messageViewHolder,
      GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      ToggleMessageOnMapInteractorFactory toggleMessageOnMapInteractorFactory, Navigator navigator,
      GlideLoader glideLoader, MessagePresentation presentation) {
    mMessageViewHolder = messageViewHolder;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mToggleMessageOnMapInteractorFactory = toggleMessageOnMapInteractorFactory;
    mNavigator = navigator;
    mGliderLoader = glideLoader;
    mPresentation = presentation;
  }

  @Override
  public void visit(MessageText message) {
    initViewHolder();
    setMessageDetails(message);
    setContent(message);
  }

  @Override
  public void visit(MessageGeo message) {
    initViewHolder();
    setMessageDetails(message);
    setContent(message);
    setGeoPanel(message, message.getGeoEntity().getGeometry());
  }

  @Override
  public void visit(MessageImage message) {
    initViewHolder();
    setMessageDetails(message);
    setContent(message);
    if (message instanceof MessageGeoImage) {
      setGeoPanel(message, ((MessageGeoImage) message).getGeoEntity().getGeometry());
    }
  }

  @Override
  public void visit(MessageAlert message) {
    initViewHolder();
    setMessageDetails(message);
    setContent(message);
    if (message.getAlert() instanceof GeoAlert) {
      GeoAlert geoAlert = (GeoAlert) message.getAlert();
      setGeoPanel(message, geoAlert.getEntity().getGeometry());
    }
  }

  @Override
  public void visit(MessageUserLocation message) {
    throw new RuntimeException("UserLocation messages should not be binded to whatsapp messages");
  }

  @Override
  public void visit(MessageVectorLayer message) {
    throw new RuntimeException("VectorLayer messages should not be binded to whatsapp messages");
  }

  @Override
  public void visit(MessageSensor message) {
    throw new RuntimeException("Sensor messages should not be binded to whatsapp messages");
  }

  private void initViewHolder() {
    setImageViewVisibility(View.GONE);
    setGeoPanelVisibility(View.GONE);
  }

  private void setMessageDetails(Message message) {
    setSenderName(message);
    setDate(message);
  }

  private void setSenderName(Message message) {
    mMessageViewHolder.senderTV.setText(message.getSenderId());
  }

  private void setDate(Message message) {
    SimpleDateFormat sdf = new SimpleDateFormat(
        mMessageViewHolder.mAppContext.getString(R.string.message_list_item_time));
    mMessageViewHolder.timeTV.setText(sdf.format(message.getCreatedAt()));
  }

  private void setGeoPanel(Message message, Geometry geometry) {
    setGeoPanelVisibility(View.VISIBLE);
    bindGeoPanel(geometry, message.getMessageId());
    updateDisplayToggle();
  }

  private void setContent(MessageGeo messageGeo) {
    setTextContent(messageGeo.getGeoEntity().getText());
  }

  private void setContent(MessageText message) {
    setTextContent(message.getText());
  }

  private void setContent(MessageImage message) {
    setImageUrl(message);
    setImageViewVisibility(View.VISIBLE);
    setTextContent(STRING_EMPTY);
    bindImageClick(message);
  }

  private void setContent(MessageAlert message) {
    Alert alert = message.getAlert();
    String text = getAlertText(alert);
    setTextContent(text);
  }

  private String getAlertText(Alert alert) {
    if (alert instanceof VectorLayerAlert) {
      VectorLayerAlert vlAlert = (VectorLayerAlert) alert;
      return mMessageViewHolder.mAppContext.getString(R.string.vector_layer_alert_message_template,
          vlAlert.getVectorLayer().getName());
    } else {
      return alert.getText();
    }
  }

  private void setImageUrl(MessageImage message) {
    Uri imageURI = getImageURI(message);

    mGliderLoader.loadImage(imageURI, mMessageViewHolder.imageView,
        mMessageViewHolder.progressView);
  }

  private Uri getImageURI(MessageImage message) {
    String url = message.getImageMetadata().getRemoteUrl();
    return Uri.parse(url);
  }

  private void setTextContent(String text) {
    mMessageViewHolder.contentTV.setText(text);
  }

  private void setImageViewVisibility(int visibility) {
    mMessageViewHolder.imageContainerLayout.setVisibility(visibility);
  }

  private void setGeoPanelVisibility(int visibility) {
    mMessageViewHolder.messageGeoPanel.setVisibility(visibility);
  }

  private void bindGeoPanel(Geometry geometry, String messageId) {
    bindGoto(geometry);
    bindDisplayToggle(messageId);
  }

  private void bindDisplayToggle(final String messageId) {
    mMessageViewHolder.displayToggleButton.setOnClickListener(
        v -> mToggleMessageOnMapInteractorFactory.create(messageId).execute());
  }

  private void bindGoto(final Geometry geometry) {
    mMessageViewHolder.gotoButton.setOnClickListener(
        v -> mGoToLocationMapInteractorFactory.create(geometry).execute());
  }

  private void updateDisplayToggle() {
    mMessageViewHolder.displayToggleButton.setChecked(mPresentation.isShownOnMap());
  }

  private void bindImageClick(final MessageImage message) {
    mMessageViewHolder.imageContainerLayout.setOnClickListener(
        v -> mNavigator.navigateToFullScreenImage(getImageURI(message)));
  }
}

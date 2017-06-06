package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import android.net.Uri;
import android.view.View;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ToggleMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.messages.MessagePresentation;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.MessageVectorLayer;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageViewHolderBindVisitor implements IMessageFeatureVisitor {

  private static final String STRING_EMPTY = "";

  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final ToggleMessageOnMapInteractorFactory mToggleMessageOnMapInteractorFactory;
  private final Navigator mNavigator;
  private final MessagesRecyclerViewAdapter.MessageViewHolder mMessageViewHolder;
  private final GlideLoader mGliderLoader;
  private final MessagePresentation mPresentation;

  public MessageViewHolderBindVisitor(MessagesRecyclerViewAdapter.MessageViewHolder messageViewHolder,
      GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      ToggleMessageOnMapInteractorFactory toggleMessageOnMapInteractorFactory,
      Navigator navigator,
      GlideLoader glideLoader,
      MessagePresentation presentation) {
    mMessageViewHolder = messageViewHolder;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mToggleMessageOnMapInteractorFactory = toggleMessageOnMapInteractorFactory;
    mNavigator = navigator;
    mGliderLoader = glideLoader;
    mPresentation = presentation;

    initViewHolder();
  }

  @Override
  public void visit(TextFeature feature) {
    setTextContent(feature.getText());
  }

  @Override
  public void visit(GeoFeature feature) {
    setGeoPanel(feature.getGeoEntity().getGeometry(), mPresentation.getMessage().getMessageId());
  }

  @Override
  public void visit(ImageFeature feature) {
    setImageContent(feature);
  }

  @Override
  public void visit(AlertFeature feature) {
  }

  @Override
  public void visit(MessageUserLocation message) {
    throw new RuntimeException("UserLocation messages should not be binded to whatsapp messages");
  }

  @Override
  public void visit(MessageVectorLayer message) {
    throw new RuntimeException("VectorLayer messages should not be binded to whatsapp messages");
  }

  private void initViewHolder() {
    setImageViewVisibility(View.GONE);
    setGeoPanelVisibility(View.GONE);
    setMessageDetails(mPresentation.getMessage());
  }

  private void setMessageDetails(Message message) {
    setSenderName(message.getSenderId());
    setDate(message.getCreatedAt());
  }

  private void setSenderName(String senderName) {
    mMessageViewHolder.senderTV.setText(senderName);
  }

  private void setDate(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat(
        mMessageViewHolder.mAppContext.getString(R.string.message_list_item_time));
    mMessageViewHolder.timeTV.setText(sdf.format(date));
  }

  private void setGeoPanel(Geometry geometry, String messageId) {
    setGeoPanelVisibility(View.VISIBLE);
    bindGeoPanel(geometry, messageId);
    updateDisplayToggle();
  }

  private void setImageContent(ImageFeature feature) {
    Uri imageURI = getImageURI(feature);
    setImageUrl(imageURI);
    setImageViewVisibility(View.VISIBLE);
    setTextContent(STRING_EMPTY);
    bindImageClick(imageURI);
  }

  private Uri getImageURI(ImageFeature feature) {
    String url = feature.getRemoteUrl();
    return Uri.parse(url);
  }

  private void setImageUrl(Uri imageURI) {
    mGliderLoader.loadImage(imageURI, mMessageViewHolder.imageView,
        mMessageViewHolder.progressView);
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

  private void bindImageClick(Uri imageURI) {
    mMessageViewHolder.imageContainerLayout.setOnClickListener(
        v -> mNavigator.navigateToFullScreenImage(imageURI));
  }
}

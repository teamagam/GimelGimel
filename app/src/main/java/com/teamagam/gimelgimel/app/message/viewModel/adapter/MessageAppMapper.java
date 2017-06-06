package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.message.model.MessageAlertApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.MessagePresentation;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageSensor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.MessageVectorLayer;
import com.teamagam.gimelgimel.domain.messages.entity.contents.GeoImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import javax.inject.Inject;

/**
 * Mapper class used to transform
 * <p>
 * {@link MessageApp} (in the app layer) to {@link Message} in the
 * domain layer.
 */
@PerActivity
public class MessageAppMapper {

  @Inject
  public MessageAppMapper() {
  }

  public MessageApp transformToModel(MessagePresentation message) {
    return new MessageToAppTransformer().transformToApp(message.getMessage(), message.isFromSelf(),
        message.isShownOnMap(), message.isNotified(), message.isSelected());
  }

  private class MessageToAppTransformer implements IMessageVisitor {

    MessageApp mMessageModel;

    private MessageApp transformToApp(Message message, boolean isFromSelf, boolean isShownOnMap,
        boolean isNotified, boolean isSelected) {
      message.accept(this);
      mMessageModel.setCreatedAt(message.getCreatedAt());
      mMessageModel.setMessageId(message.getMessageId());
      mMessageModel.setSenderId(message.getSenderId());
      mMessageModel.setIsNotified(isNotified);
      mMessageModel.setSelected(isSelected);
      mMessageModel.setFromSelf(isFromSelf);
      mMessageModel.setShownOnMap(isShownOnMap);
      return mMessageModel;
    }

    @Override
    public void visit(MessageText message) {
      mMessageModel = new MessageTextApp(message.getText());
    }

    @Override
    public void visit(final MessageGeo message) {
      GeoContentApp geoContentApp = new GeoContentApp(message.getGeoEntity());
      mMessageModel = new MessageGeoApp(geoContentApp);
    }

    @Override
    public void visit(MessageImage message) {
      ImageMetadata meta = message.getImageMetadata();
      GeoEntity geoEntity = null;
      if (meta instanceof GeoImageMetadata) {
        geoEntity = ((GeoImageMetadata) meta).getGeoEntity();
      }
      ImageMetadataApp imageMetadataApp =
          new ImageMetadataApp(meta.getTime(), meta.getRemoteUrl(), meta.getSource(), geoEntity);
      mMessageModel = new MessageImageApp(imageMetadataApp);
    }

    @Override
    public void visit(MessageSensor message) {
      //nothing
    }

    @Override
    public void visit(MessageAlert messageAlert) {
      mMessageModel = new MessageAlertApp(messageAlert.getAlert());
    }

    @Override
    public void visit(MessageVectorLayer message) {
      throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    public void visit(MessageUserLocation message) {
      throw new UnsupportedOperationException("method not implemented!");
    }
  }
}

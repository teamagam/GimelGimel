package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper class used to transform
 * <p>
 * {@link MessageApp} (in the app layer) to {@link Message} in the
 * domain layer.
 */
@PerActivity
public class MessageAppMappper {

    MessageToAppTransformer transformer;

    @Inject
    public MessageAppMappper() {
        transformer = new MessageToAppTransformer();
    }

    public MessageApp transformToModel(Message message) {
        return transformer.transformToApp(message);
    }

    /**
     * Transform a {@link MessageData} into an {@link com.teamagam.gimelgimel.domain.messages.entity.Message}.
     *
     * @param message Object to be transformed.
     * @return {@link com.teamagam.gimelgimel.domain.messages.entity.Message} if valid {@link MessageData} otherwise null.
     */
    public Message transform(MessageApp message) {
        switch (message.getType()) {
            case MessageData.GEO: {
                return createMessageGeo(message);
            }
            case MessageData.IMAGE: {
                //todo: return message image
            }
            case MessageData.TEXT: {
                return createMessageText(message);
            }
            case MessageData.USER_LOCATION: {
                //todo: return message user location
            }
            default:
                return null;
        }
    }

    /**
     * Transform a List of {@link MessageApp} into a Collection of {@link Message}.
     *
     * @param messageCollection Object Collection to be transformed.
     * @return {@link Message} if valid {@link MessageData} otherwise null.
     */
    public List<Message> transform(Collection<MessageApp> messageCollection) {
        List<Message> messageList = new ArrayList<>(20);
        Message messageModel;
        for (MessageApp message : messageCollection) {
            messageModel = transform(message);
            if (messageModel != null) {
                messageList.add(messageModel);
            }
        }

        return messageList;
    }


    private MessageText createMessageText(MessageApp message) {
        MessageTextApp messageText = (MessageTextApp) message;
        return new MessageText(message.getMessageId(), message.getSenderId(), message.getCreatedAt(),
                messageText.getContent());
    }

    private MessageGeo createMessageGeo(MessageApp message) {
        GeoContentApp geoContent = ((MessageGeoApp) message).getContent();
        PointGeometry convertedPoint = convertPointGeometry(geoContent.getPointGeometry());
        PointSymbol symbol = new PointSymbol(message.getType());
        GeoEntity geoEntity = createGeoEntity(convertedPoint, symbol);

        return new MessageGeo(message.getSenderId(),
                message.getSenderId(), message.getCreatedAt(), geoEntity, geoContent.getText(), message.getType());

    }

    private GeoEntity createGeoEntity(PointGeometry geometry,
                                      PointSymbol symbol) {
        // FIXME: 10/6/2016   // FIXME: 10/6/2016
        return new PointEntity(null, null, geometry, symbol);
    }

    private PointGeometry convertPointGeometry(PointGeometryApp pointGeometry) {
        PointGeometry convertedPoint =
                new PointGeometry(pointGeometry.latitude, pointGeometry.longitude);

        if (pointGeometry.hasAltitude) {
            convertedPoint.setAltitude(pointGeometry.altitude);
        }

        return convertedPoint;
    }

    private class MessageToAppTransformer implements IMessageVisitor {

        MessageApp mMessageModel;

        private MessageApp transformToApp(Message message) {
            message.accept(this);
            mMessageModel.setCreatedAt(message.getCreatedAt());
            mMessageModel.setMessageId(message.getMessageId());
            mMessageModel.setSenderId(message.getSenderId());
            mMessageModel.setSelected(message.isSelected());
            mMessageModel.setRead(message.isRead());
            return mMessageModel;
        }

        @Override
        public void visit(MessageUserLocation message) {
//            LocationSampleData locationSampleData = transformToApp(message.getLocationSample());
//            mMessageModel = new MessageUserLocationData(locationSampleData);
        }

        @Override
        public void visit(MessageGeo message) {
            PointGeometryApp pointGeometry = transformGeoEntityToPoint(message.getGeoEntity().getGeometry());
            GeoContentApp geoContent = new GeoContentApp(pointGeometry, message.getText(), message.getType());
            mMessageModel = new MessageGeoApp(geoContent);
        }

        private PointGeometryApp transformGeoEntityToPoint(Geometry geometry) {
            PointGeometry point = (PointGeometry) geometry;
            return new PointGeometryApp(point.getLatitude(), point.getLongitude(), point.getAltitude());
        }

        @Override
        public void visit(MessageText message) {
            mMessageModel = new MessageTextApp(message.getSenderId(), message.getText());
        }

        @Override
        public void visit(MessageImage message) {
//            ImageMetadataData imageMetadata = transformMetadataToData(message.getImageMetadata());
//            mMessageModel = new MessageImageData(imageMetadata);
        }

    }
}

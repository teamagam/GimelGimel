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

    MessageToModelTransformer transformer;

    @Inject
    public MessageAppMappper() {
        transformer = new MessageToModelTransformer();
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
            }
            case MessageData.TEXT: {
            }
            case MessageData.USER_LOCATION: {

            }
            default:
                return null;
        }
    }

    /**
     * Transform a List of {@link MessageData} into a Collection of {@link Message}.
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

    private MessageGeo createMessageGeo(MessageApp message) {
        GeoContentApp geoContent = ((MessageGeoApp) message).getContent();
        PointGeometry convertedPoint = convertPointGeometry(geoContent.getPointGeometry());
        PointSymbol symbol = new PointSymbol(message.getType());
        GeoEntity geoEntity = createGeoEntity(convertedPoint, symbol);

        MessageGeo geo = new MessageGeo(message.getSenderId(),
                geoEntity, geoContent.getText(), message.getType());
        geo.setCreatedAt(message.getCreatedAt());

        return geo;
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

    private class MessageToModelTransformer implements IMessageVisitor {

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

        private PointGeometryApp
        transformGeoEntityToPoint(Geometry geometry) {
            PointGeometry point = (PointGeometry) geometry;
            return new PointGeometryApp(point
                    .getLatitude(), point.getLongitude(), point.getAltitude());
        }

        @Override
        public void visit(MessageText message) {
            mMessageModel = new MessageTextApp(message
                    .getSenderId(),
                    message.getText());
        }

        @Override
        public void visit(MessageImage message) {
//            ImageMetadataData imageMetadata = transformMetadataToData(message.getImageMetadata());
//            mMessageModel = new MessageImageData(imageMetadata);
        }

    }
}

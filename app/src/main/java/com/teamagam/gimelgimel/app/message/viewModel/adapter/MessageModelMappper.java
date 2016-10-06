package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import com.teamagam.gimelgimel.app.message.model.MessageGeoModel;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;
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
import javax.inject.Singleton;

/**
 * Mapper class used to transform
 * <p>
 * {@link com.teamagam.gimelgimel.app.model.ViewsModels.Message} (in the app layer) to {@link Message} in the
 * domain layer.
 */
@Singleton
public class MessageModelMappper {

    MessageToModelTransformer transformer;

    @Inject
    public MessageModelMappper() {
        transformer = new MessageToModelTransformer();
    }

    public com.teamagam.gimelgimel.app.model.ViewsModels.Message transformToModel(Message message) {
        return transformer.transformToModel(message);
    }

    /**
     * Transform a {@link MessageData} into an {@link com.teamagam.gimelgimel.domain.messages.entity.Message}.
     *
     * @param message Object to be transformed.
     * @return {@link com.teamagam.gimelgimel.domain.messages.entity.Message} if valid {@link MessageData} otherwise null.
     */
    public Message transform(com.teamagam.gimelgimel.app.model.ViewsModels.Message message) {
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
    public List<Message> transform(Collection<com.teamagam.gimelgimel.app.model.ViewsModels.Message> messageCollection) {
        List<Message> messageList = new ArrayList<>(20);
        Message messageModel;
        for (com.teamagam.gimelgimel.app.model.ViewsModels.Message message : messageCollection) {
            messageModel = transform(message);
            if (messageModel != null) {
                messageList.add(messageModel);
            }
        }

        return messageList;
    }

    private MessageGeo createMessageGeo(com.teamagam.gimelgimel.app.model.ViewsModels.Message message) {
        GeoContent geoContent = ((MessageGeoModel) message).getContent();
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

    private PointGeometry convertPointGeometry(com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry pointGeometry) {
        PointGeometry convertedPoint =
                new PointGeometry(pointGeometry.latitude, pointGeometry.longitude);

        if (pointGeometry.hasAltitude) {
            convertedPoint.setAltitude(pointGeometry.altitude);
        }

        return convertedPoint;
    }

    private class MessageToModelTransformer implements IMessageVisitor {

        com.teamagam.gimelgimel.app.model.ViewsModels.Message mMessageModel;

        private com.teamagam.gimelgimel.app.model.ViewsModels.Message transformToModel(Message message) {
            message.accept(this);
            mMessageModel.setCreatedAt(message.getCreatedAt());
            mMessageModel.setMessageId(message.getMessageId());
            mMessageModel.setSenderId(message.getSenderId());
            return mMessageModel;
        }

        @Override
        public void visit(MessageUserLocation message) {
//            LocationSampleData locationSampleData = transformToModel(message.getLocationSample());
//            mMessageModel = new MessageUserLocationData(locationSampleData);
        }

        @Override
        public void visit(MessageGeo message) {
            com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry pointGeometry = transformGeoEntityToPoint(message.getGeoEntity().getGeometry());
            GeoContent geoContent = new GeoContent(pointGeometry, message.getText(), message.getType());
            mMessageModel = new MessageGeoModel(geoContent);
        }

        private com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry
        transformGeoEntityToPoint(Geometry geometry) {
            PointGeometry point = (PointGeometry) geometry;
            return new com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry(point
                    .getLatitude(), point.getLongitude(), point.getAltitude());
        }

        @Override
        public void visit(MessageText message) {
//            mMessageModel = new MessageTextData(message.getText());
        }

        @Override
        public void visit(MessageImage message) {
//            ImageMetadataData imageMetadata = transformMetadataToData(message.getImageMetadata());
//            mMessageModel = new MessageImageData(imageMetadata);
        }

    }
}

package com.teamagam.gimelgimel.data.message.adapters;


import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.map.entity.mapper.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper class used to transform {@link MessageData} (in the data layer) to {@link com.teamagam.gimelgimel.domain.messages.entity.Message} in the
 * domain layer.
 */
@Singleton
public class MessageDataMapper {

    GeometryDataMapper mGeometryDataMapper;

    MessageToDataTransformer transformer;

    @Inject
    public MessageDataMapper(GeometryDataMapper geometryDataMapper) {
        mGeometryDataMapper = geometryDataMapper;
        transformer = new MessageToDataTransformer();
    }

    public MessageData transformToData(Message message) {
        return transformer.transformToData(message);
    }

    /**
     * Transform a {@link MessageData} into an {@link com.teamagam.gimelgimel.domain.messages.entity.Message}.
     *
     * @param message Object to be transformed.
     * @return {@link com.teamagam.gimelgimel.domain.messages.entity.Message} if valid {@link MessageData} otherwise null.
     */
    public Message transform(MessageData message) {
        switch (message.getType()) {
            case MessageData.GEO: {
                return createMessageGeo(message);
            }
            case MessageData.IMAGE: {
                return createMessageImage(message);
            }
            case MessageData.TEXT: {
                return createMessageText(message);
            }
            case MessageData.USER_LOCATION: {
                return createMessageUserLocation(message);
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
    public List<Message> transform(Collection<MessageData> messageCollection) {
        List<Message> messageList = new ArrayList<>(20);
        Message messageModel;
        for (MessageData message : messageCollection) {
            messageModel = transform(message);
            if (messageModel != null) {
                messageList.add(messageModel);
            }
        }

        return messageList;
    }


    private MessageUserLocation createMessageUserLocation(MessageData message) {
        LocationSampleData content =
                (LocationSampleData) message.getContent();
        LocationSampleEntity convertedLocationSampleEntity = convertLocationSample(content);

        MessageUserLocation userLocation =
                new MessageUserLocation(message.getSenderId(), convertedLocationSampleEntity);
        userLocation.setCreatedAt(message.getCreatedAt());

        return userLocation;
    }

    private MessageText createMessageText(MessageData message) {
        String content = (String) message.getContent();

        MessageText text = new MessageText(message.getSenderId(), content);
        text.setCreatedAt(message.getCreatedAt());

        return text;
    }

    private MessageImage createMessageImage(MessageData message) {
        ImageMetadataData content =
                (ImageMetadataData) message.getContent();
        ImageMetadata convertedImageMetadata =
                convertImageMetadata(content);

        MessageImage image = new MessageImage(message.getSenderId(), convertedImageMetadata);
        image.setCreatedAt(message.getCreatedAt());

        return image;
    }

    private MessageGeo createMessageGeo(MessageData message) {
        GeoContentData content = (GeoContentData) message.getContent();
        PointGeometry convertedPoint = convertPointGeometry(content.getPointGeometry());
        PointSymbol symbol = new PointSymbol(content.getType());
        GeoEntity geoEntity = createGeoEntity(content.getText(), convertedPoint, symbol);

        MessageGeo geo = new MessageGeo(message.getSenderId(),
                geoEntity, content.getText(), message.getType());
        geo.setCreatedAt(message.getCreatedAt());

        return geo;
    }

    private GeoEntity createGeoEntity(String id, PointGeometry geometry, PointSymbol symbol) {
        return new PointEntity(id, null, geometry, symbol);
    }

    private LocationSampleEntity convertLocationSample(LocationSampleData content) {
        LocationSampleEntity convertedLocationSampleEntity =
                new LocationSampleEntity(convertPointGeometry(content.getLocation()),
                        content.getTime());

        if (content.hasAccuracy()) {
            convertedLocationSampleEntity.setAccuracy(content.getAccuracy());
        }
        if (content.hasBearing()) {
            convertedLocationSampleEntity.setBearing(content.getBearing());
        }
        if (content.hasSpeed()) {
            convertedLocationSampleEntity.setSpeed(content.getSpeed());
        }

        return convertedLocationSampleEntity;
    }

    private ImageMetadata convertImageMetadata(ImageMetadataData content) {
        ImageMetadata convertedImageMetadata =
                new ImageMetadata(
                        content.getTime(), content.getURL(), content.getSource());

        if (content.hasLocation()) {
            convertedImageMetadata.setLocation(convertPointGeometry(content.getLocation()));
        }

        return convertedImageMetadata;
    }

    private PointGeometry convertPointGeometry(PointGeometryData pointGeometry) {
        PointGeometry convertedPoint = new PointGeometry(pointGeometry.latitude,
                pointGeometry.longitude);

        if (pointGeometry.hasAltitude) {
            convertedPoint.setAltitude(pointGeometry.altitude);
        }

        return convertedPoint;
    }

    private class MessageToDataTransformer implements IMessageVisitor {

        MessageData mMessageData;

        private MessageData transformToData(Message message) {
            message.accept(this);
            mMessageData.setCreatedAt(message.getCreatedAt());
            mMessageData.setMessageId(message.getMessageId());
            mMessageData.setSenderId(message.getSenderId());
            return mMessageData;
        }

        @Override
        public void visit(MessageUserLocation message) {
            LocationSampleData locationSampleData = transformToData(message.getLocationSample());
            mMessageData = new MessageUserLocationData(locationSampleData);
        }

        @Override
        public void visit(MessageGeo message) {
            PointGeometryData pointData =
                    transformPointGeometry((PointGeometry) message.getGeoEntity().getGeometry());
            GeoContentData content = new GeoContentData(pointData, message.getText(),
                    message.getType());
            mMessageData = new MessageGeoData(content);
        }

        @Override
        public void visit(MessageText message) {
            mMessageData = new MessageTextData(message.getText());
        }

        @Override
        public void visit(MessageImage message) {
            ImageMetadataData imageMetadata = transformMetadataToData(message.getImageMetadata());
            mMessageData = new MessageImageData(imageMetadata);
        }

        private LocationSampleData transformToData(LocationSampleEntity locationSampleEntity) {
            PointGeometryData pointGeometryData =
                    transformPointGeometry(locationSampleEntity.getLocation());
            return new LocationSampleData(locationSampleEntity, pointGeometryData);
        }

        private ImageMetadataData transformMetadataToData(ImageMetadata imageMetadata) {
            if (imageMetadata.hasLocation()) {
                PointGeometryData pointGeometryData =
                        transformPointGeometry(imageMetadata.getLocation());
                return new ImageMetadataData(imageMetadata, pointGeometryData);
            } else {
                return new ImageMetadataData(imageMetadata);
            }
        }

        private PointGeometryData transformPointGeometry(PointGeometry point) {
            return (PointGeometryData) mGeometryDataMapper.transformToData(point);
        }
    }
}
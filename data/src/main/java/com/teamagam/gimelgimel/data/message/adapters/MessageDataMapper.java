package com.teamagam.gimelgimel.data.message.adapters;


import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageSensorData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.data.message.entity.contents.SensorMetadataData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageSensor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
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

    private final LocationSampleDataAdapter mLocationSampleAdapter;
    private final GeometryDataMapper mGeometryDataMapper;
    private final GeoEntityDataMapper mGeoEntityDataMapper;

    @Inject
    public MessageDataMapper(GeometryDataMapper geometryDataMapper, LocationSampleDataAdapter
            locationSampleAdapter, GeoEntityDataMapper geoEntityDataMapper) {
        mGeometryDataMapper = geometryDataMapper;
        mLocationSampleAdapter = locationSampleAdapter;
        mGeoEntityDataMapper = geoEntityDataMapper;
    }

    public MessageData transformToData(Message message) {
        return new MessageToDataTransformer().transformToData(message);
    }

    /**
     * Transform a {@link MessageData} into an {@link Message}.
     *
     * @param message Object to be transformed.
     * @return {@link com.teamagam.gimelgimel.domain.messages.entity.Message} if valid {@link MessageData} otherwise null.
     */
    public Message transform(MessageData message) {
        return new MessageFromDataTransformer().transformFromData(message);
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

    private class MessageFromDataTransformer implements IMessageDataVisitor {

        private static final String EMPTY_STRING = "";
        Message mMessage;

        private Message transformFromData(MessageData msgData) {
            msgData.accept(this);
            return mMessage;
        }

        @Override
        public void visit(MessageTextData message) {
            String text = message.getContent();
            mMessage = new MessageText(message.getMessageId(),
                    message.getSenderId(), message.getCreatedAt(),
                    text);
        }

        @Override
        public void visit(MessageGeoData message) {
            GeoEntity geoEntity = mGeoEntityDataMapper.transform(message.getMessageId(),
                    message.getContent());

            mMessage = new MessageGeo(message.getMessageId(),
                    message.getSenderId(),
                    message.getCreatedAt(),
                    geoEntity);
        }

        @Override
        public void visit(MessageImageData message) {
            ImageMetadata imageMetadata = convertImageMetadata(message.getContent(),
                    message.getMessageId());
            mMessage = new MessageImage(message.getMessageId(),
                    message.getSenderId(), message.getCreatedAt(),
                    imageMetadata);
        }

        @Override
        public void visit(MessageSensorData message) {
            SensorMetadata sensorMetadata = convertSensorMetaData(message.getContent(),
                    message.getMessageId());
            mMessage = new MessageSensor(message.getMessageId(), message.getSenderId(),
                    message.getCreatedAt(),
                    sensorMetadata);
        }

        private SensorMetadata convertSensorMetaData(SensorMetadataData sensorMetadataData,
                                                     String id) {
            SensorEntity se = mGeoEntityDataMapper.transformIntoSensorEntity(id,
                    sensorMetadataData.getPointGeometryData());
            return new SensorMetadata(sensorMetadataData.getId(), sensorMetadataData.getName(), se);
        }

        private ImageMetadata convertImageMetadata(ImageMetadataData content, String id) {
            ImageEntity imageEntity = null;
            if (content.hasLocation()) {
                imageEntity = mGeoEntityDataMapper.transformIntoImageEntity(id,
                        content.getLocation());
            }
            return new ImageMetadata(
                    content.getTime(), content.getRemoteUrl(), EMPTY_STRING, imageEntity,
                    content.getSource());
        }

        @Override
        public void visit(MessageUserLocationData message) {
            LocationSample convertedLocationSample = mLocationSampleAdapter.transform(
                    message.getContent());
            mMessage = new MessageUserLocation(message.getMessageId(),
                    message.getSenderId(), message.getCreatedAt(),
                    convertedLocationSample);
        }
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
        public void visit(MessageText message) {
            mMessageData = new MessageTextData(message.getText());
        }

        @Override
        public void visit(MessageGeo message) {
            GeoContentData content = mGeoEntityDataMapper.transform(message.getGeoEntity());
            GeoContentData geoContentData = new GeoContentData((PointGeometryData) content
                    .getGeometry(), content.getText(), content.getType());
            mMessageData = new MessageGeoData(geoContentData);
        }

        @Override
        public void visit(MessageImage message) {
            ImageMetadataData imageMetadata = transformMetadataToData(message.getImageMetadata());
            mMessageData = new MessageImageData(imageMetadata);
        }

        @Override
        public void visit(MessageSensor message) {
            SensorMetadataData sensorMetadata = transformSensorMetadataToData(
                    message.getSensorMetadata());
            mMessageData = new MessageSensorData(sensorMetadata);
        }

        @Override
        public void visit(MessageUserLocation message) {
            LocationSampleData locationSampleData =
                    mLocationSampleAdapter.transformToData(message.getLocationSample());
            mMessageData = new MessageUserLocationData(locationSampleData);
        }

        //those should'nt be here`
        private ImageMetadataData transformMetadataToData(ImageMetadata imageMetadata) {
            PointGeometryData pointGeometryData = null;
            if (imageMetadata.hasLocation()) {
                GeoContentData geoContentData = mGeoEntityDataMapper.transform(
                        imageMetadata.getGeoEntity());
                pointGeometryData = (PointGeometryData) geoContentData.getGeometry();
            }
            return new ImageMetadataData(imageMetadata.getTime(), null, imageMetadata.getLocalUrl(),
                    pointGeometryData, imageMetadata.getSource());
        }

        private SensorMetadataData transformSensorMetadataToData(SensorMetadata sensorData) {
            GeoContentData geoContentData = mGeoEntityDataMapper.transform(
                    sensorData.getGeoEntity());
            return new SensorMetadataData(sensorData.getId(), sensorData.getName(),
                    (PointGeometryData) geoContentData.getGeometry());
        }
    }
}
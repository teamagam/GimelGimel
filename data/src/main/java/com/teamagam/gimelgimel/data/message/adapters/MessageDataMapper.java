package com.teamagam.gimelgimel.data.message.adapters;


import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;
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

    private final LocationSampleDataAdapter mLocationSampleAdapter;
    private final GeometryDataMapper mGeometryDataMapper;

    @Inject
    public MessageDataMapper(GeometryDataMapper geometryDataMapper, LocationSampleDataAdapter
            locationSampleAdapter) {
        mGeometryDataMapper = geometryDataMapper;
        mLocationSampleAdapter = locationSampleAdapter;
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

    private GeoEntity createGeoEntity(String id, PointGeometry geometry, PointSymbol symbol) {
        return new PointEntity(id, null, geometry, symbol);
    }

    private ImageMetadata convertImageMetadata(ImageMetadataData content) {
        ImageMetadata convertedImageMetadata =
                new ImageMetadata(
                        content.getTime(), content.getURL(), content.getSource());

        if (content.hasLocation()) {
            convertedImageMetadata.setLocation(mGeometryDataMapper.transform(content.getLocation()));
        }

        return convertedImageMetadata;
    }

    private class MessageFromDataTransformer implements IMessageDataVisitor{

        Message mMessage;

        private Message transformFromData(MessageData msgData){
            msgData.accept(this);
            return mMessage;
        }

        @Override
        public void visit(MessageTextData message) {
            String text = message.getContent();
            mMessage = new MessageText(message.getMessageId(),
                    message.getSenderId(), message.getCreatedAt(),
                    message.isRead(),
                    message.isSelected(),
                    text);
        }

        @Override
        public void visit(MessageGeoData message) {
            PointGeometry convertedPoint = mGeometryDataMapper.transform(message.getContent()
                    .getPointGeometry());
            PointSymbol symbol = new PointSymbol(message.getContent().getType());
            GeoEntity geoEntity = createGeoEntity(message.getContent().getText(), convertedPoint, symbol);

            mMessage = new MessageGeo(message.getMessageId(),
                    message.getSenderId(), message.getCreatedAt(),
                    message.isRead(),
                    message.isSelected(),
                    geoEntity, message.getContent().getText(), message.getContent().getType());
        }

        @Override
        public void visit(MessageImageData message) {
            ImageMetadata imageMetadata = convertImageMetadata(message.getContent());
            mMessage = new MessageImage(message.getMessageId(),
                    message.getSenderId(), message.getCreatedAt(),
                    message.isRead(),
                    message.isSelected(),
                    imageMetadata );
        }

        @Override
        public void visit(MessageUserLocationData message) {
            LocationSampleEntity convertedLocationSampleEntity = mLocationSampleAdapter.transform(
                    message.getContent());
            mMessage = new MessageUserLocation(message.getMessageId(),
                    message.getSenderId(), message.getCreatedAt(),
                    message.isRead(),
                    message.isSelected(),
                    convertedLocationSampleEntity);
        }
    }

    private class MessageToDataTransformer implements IMessageVisitor {

        MessageData mMessageData;

        private MessageData transformToData(Message message) {
            message.accept(this);
            mMessageData.setCreatedAt(message.getCreatedAt());
            mMessageData.setMessageId(message.getMessageId());
            mMessageData.setSenderId(message.getSenderId());
            mMessageData.setRead(message.isRead());
            mMessageData.setSelected(message.isSelected());
            return mMessageData;
        }

        @Override
        public void visit(MessageUserLocation message) {
            LocationSampleData locationSampleData =
                    mLocationSampleAdapter.transformToData(message.getLocationSample());
            mMessageData = new MessageUserLocationData(locationSampleData);
        }

        @Override
        public void visit(MessageGeo message) {
            PointGeometryData pointData =
                    mGeometryDataMapper.transformToData((PointGeometry) message.getGeoEntity().getGeometry());
            GeoContentData content = new GeoContentData(pointData, message.getText(),
                    message.getGeoEntity().getSymbol().toString());
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

        //those should'nt be here`

        //// FIXME: 10/30/2016
        private ImageMetadataData transformMetadataToData(ImageMetadata imageMetadata) {
            if (imageMetadata.hasLocation()) {
                PointGeometryData pointGeometryData =
                        mGeometryDataMapper.transformToData(imageMetadata.getLocation());
                return new ImageMetadataData(imageMetadata, pointGeometryData);
            } else {
                return new ImageMetadataData(imageMetadata);
            }
        }

    }
}
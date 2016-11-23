package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
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

    private final GeoEntityTransformer mGeoEntityTransformer;

    @Inject
    public MessageAppMapper(GeoEntityTransformer geoEntityTransformer) {
        mGeoEntityTransformer = geoEntityTransformer;
    }

    public MessageApp transformToModel(Message message) {
        return new MessageToAppTransformer().transformToApp(message);
    }

//    /**
//     * Transform a {@link MessageData} into an {@link com.teamagam.gimelgimel.domain.messages.entity.Message}.
//     *
//     * @param message Object to be transformed.
//     * @return {@link com.teamagam.gimelgimel.domain.messages.entity.Message} if valid {@link MessageData} otherwise null.
//     */
//    public Message transform(MessageApp message) {
//        switch (message.getmLocationType()) {
//            case MessageData.TEXT: {
//                return createMessageText(message);
//            }
//            case MessageData.GEO: {
//                return createMessageGeo(message);
//            }
//            case MessageData.IMAGE: {
//                return createMessageImage(message);
//            }
//            case MessageData.USER_LOCATION: {
//                throw new NullPointerException("not implemented!");
//                //todo: return message user location
//            }
//            default:
//                return null;
//        }
//    }

//    /**
//     * Transform a List of {@link MessageApp} into a Collection of {@link Message}.
//     *
//     * @param messageCollection Object Collection to be transformed.
//     * @return {@link Message} if valid {@link MessageData} otherwise null.
//     */
//    public List<Message> transform(Collection<MessageApp> messageCollection) {
//        List<Message> messageList = new ArrayList<>(20);
//        Message messageModel;
//        for (MessageApp message : messageCollection) {
//            messageModel = transform(message);
//            if (messageModel != null) {
//                messageList.add(messageModel);
//            }
//        }
//
//        return messageList;
//    }

//    private MessageText createMessageText(MessageApp message) {
//        MessageTextApp messageText = (MessageTextApp) message;
//        return new MessageText(message.getMessageId(), message.getSenderId(), message.getCreatedAt(),
//                message.isRead(), message.isSelected(), messageText.getContent());
//    }

//    private MessageGeo createMessageGeo(MessageApp message) {
//        GeoContentApp geoContent = ((MessageGeoApp) message).getContent();
//        PointGeometry convertedPoint = convertPointGeometry(geoContent.getPointGeometry());
//        PointSymbol symbol = new PointSymbol(geoContent.getmLocationType(), geoContent.getGeoContentText());
//        GeoEntity geoEntity = createGeoEntity(convertedPoint, symbol);
//
//        return new MessageGeo(message.getMessageId(),
//                message.getSenderId(), message.getCreatedAt(), message.isRead(),
//                message.isSelected(), geoEntity.getId(), geoContent.getText());
//
//    }

//    private MessageImage createMessageImage(MessageApp message) {
//        return new MessageImage(message.getMessageId(),
//                message.getSenderId(), message.getCreatedAt(),
//                message.isRead(), message.isSelected(), (ImageMetadata) message.getContent());
//    }

//    private GeoEntity createGeoEntity(PointGeometry geometry,
//                                      PointSymbol symbol) {
//        // FIXME: 10/6/2016
//        return new PointEntity(null, null, geometry, symbol);
//    }

//    private PointGeometry convertPointGeometry(PointGeometryApp pointGeometry) {
//        PointGeometry convertedPoint =
//                new PointGeometry(pointGeometry.latitude, pointGeometry.longitude);
//
//        if (pointGeometry.hasAltitude) {
//            convertedPoint.setAltitude(pointGeometry.altitude);
//        }
//
//        return convertedPoint;
//    }

    private class MessageToAppTransformer implements IMessageVisitor {

        MessageApp mMessageModel;

        private MessageApp transformToApp(Message message) {
            message.accept(this);
            mMessageModel.setCreatedAt(message.getCreatedAt());
            mMessageModel.setMessageId(message.getMessageId());
            mMessageModel.setSenderId(message.getSenderId());
            mMessageModel.setSelected(false);
            mMessageModel.setRead(false);
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
            ImageMetadataApp imageMetadataApp = new ImageMetadataApp(meta.getTime(), meta.getURL(),
                    meta.getGeoEntity(), meta.getSource());
            mMessageModel = new MessageImageApp(imageMetadataApp);
        }

        @Override
        public void visit(MessageUserLocation message) {
            throw new NullPointerException("method not implemented!");
//            LocationSampleApp locationSampleApp = transformToApp(message.getLocationSample());
//            mMessageModel = new MessageUserLocationApp(locationSampleApp);
        }
    }
}
package com.gimelgimel.data.access.http;

import com.gimelgimel.domain.model.MessageGeo;
import com.gimelgimel.domain.model.MessageImage;
import com.gimelgimel.domain.model.MessageModel;
import com.gimelgimel.domain.model.MessageText;
import com.gimelgimel.domain.model.MessageUserLocation;
import com.gimelgimel.domain.model.PointGeometry;
import com.gimelgimel.domain.model.contents.ImageMetadata;
import com.gimelgimel.domain.model.contents.LocationSample;
import com.gimelgimel.domain.repository.MessagesRepository;

import httpModels.Message;
import httpModels.contents.GeoContent;
import rx.Observable;

public class MessagesRestAPI implements MessagesRepository {

    private GGMessagingAPI mRestAPI;

    public MessagesRestAPI(GGMessagingAPI api) {
        mRestAPI = api;
    }

    @Override
    public Observable<MessageModel> getMessages() {
        return mRestAPI.getMessages()
                .flatMapIterable(messages -> messages)
                .map(message -> createMessageModel(message));
    }

    @Override
    public Observable<MessageModel> putMessage(MessageModel messageData) {
        return null;
    }

    @Override
    public Observable<MessageModel> sendMessage(MessageModel message) {
        //return mRestAPI.postMessage();
        return null;
    }

    private MessageModel createMessageModel(Message message) {
        switch (message.getType()) {
            case Message.GEO: {
                return createMessageGeo(message);
            }
            case Message.IMAGE: {
                return createMessageImage(message);
            }
            case Message.TEXT: {
                return createMessageText(message);
            }
            case Message.USER_LOCATION: {
                return createMessageUserLocation(message);
            }
            default:
                return null;
        }
    }

    private MessageUserLocation createMessageUserLocation(Message message) {
        httpModels.contents.LocationSample content =
                (httpModels.contents.LocationSample) message.getContent();
        LocationSample convertedLocationSample = convertLocationSample(content);

        MessageUserLocation userLocation =
                new MessageUserLocation(message.getSenderId(), convertedLocationSample);
        userLocation.setCreatedAt(message.getCreatedAt());

        return userLocation;
    }

    private MessageText createMessageText(Message message) {
        String content = (String) message.getContent();

        MessageText text = new MessageText(message.getSenderId(), content);
        text.setCreatedAt(message.getCreatedAt());

        return text;
    }

    private MessageImage createMessageImage(Message message) {
        httpModels.contents.ImageMetadata content =
                (httpModels.contents.ImageMetadata) message.getContent();
        ImageMetadata convertedImageMetadata =
                 convertImageMetadata(content);

        MessageImage image = new MessageImage(message.getSenderId(), convertedImageMetadata);
        image.setCreatedAt(message.getCreatedAt());

        return image;
    }

    private MessageGeo createMessageGeo(Message message) {
        GeoContent content = (GeoContent) message.getContent();
        PointGeometry convertedPoint = convertPointGeometry(content.getPointGeometry());

        MessageGeo geo = new MessageGeo(message.getSenderId(),
                convertedPoint, content.getText(), message.getType());
        geo.setCreatedAt(message.getCreatedAt());

        return geo;
    }

    private LocationSample convertLocationSample(httpModels.contents.LocationSample content) {
        LocationSample convertedLocationSample =
                new LocationSample(convertPointGeometry(content.getLocation()), content.getTime());

        if(content.hasAccuracy()) {
            convertedLocationSample.setAccuracy(content.getAccuracy());
        }
        if(content.hasBearing()) {
            convertedLocationSample.setBearing(content.getBearing());
        }
        if(content.hasSpeed()) {
            convertedLocationSample.setSpeed(content.getSpeed());
        }

        return convertedLocationSample;
    }

    private ImageMetadata convertImageMetadata(httpModels.contents.ImageMetadata content) {
        ImageMetadata convertedImageMetadata =
                new com.gimelgimel.domain.model.contents.ImageMetadata(
                        content.getTime(), content.getURL(), content.getSource());

        if (content.hasLocation()) {
            convertedImageMetadata.setLocation(convertPointGeometry(content.getLocation()));
        }

        return convertedImageMetadata;
    }

    private PointGeometry convertPointGeometry(httpModels.geometries.PointGeometry pointGeometry) {
        PointGeometry convertedPoint = new PointGeometry(pointGeometry.latitude, pointGeometry.longitude);

        if (pointGeometry.hasAltitude) {
            convertedPoint.setAltitude(pointGeometry.altitude);
        }

        return convertedPoint;
    }
}

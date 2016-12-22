package com.teamagam.gimelgimel.app.message.viewModel.adapter;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.app.sensor.model.MessageSensorApp;
import com.teamagam.gimelgimel.app.sensor.model.SensorMetadataApp;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageSensor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
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
            ImageMetadataApp imageMetadataApp = new ImageMetadataApp(meta.getTime(),
                    meta.getRemoteUrl(),
                    meta.getGeoEntity(), meta.getSource());
            mMessageModel = new MessageImageApp(imageMetadataApp);
        }

        @Override
        public void visit(MessageSensor message) {
            SensorMetadata sensorData = message.getSensorMetadata();
            SensorMetadataApp sma = new SensorMetadataApp(sensorData.getId(), sensorData.getName(),
                    sensorData.getGeoEntity());
            mMessageModel = new MessageSensorApp(sma);
        }

        @Override
        public void visit(MessageAlert messageAlert) {
            throw new RuntimeException("Mapper from MessageAlert to app is not supported ");
        }

        @Override
        public void visit(MessageUserLocation message) {
            throw new NullPointerException("method not implemented!");
//            LocationSampleApp locationSampleApp = transformToApp(message.getLocationSample());
//            mMessageModel = new MessageUserLocationApp(locationSampleApp);
        }
    }
}

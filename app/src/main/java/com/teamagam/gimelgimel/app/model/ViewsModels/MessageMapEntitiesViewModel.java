package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds message pin locations.
 */
public class MessageMapEntitiesViewModel implements Entity.OnClickListener {

    private final SelectedMessageModel mSelectedModel;
    private Map<Entity, Message> mEntityToMessageHashMap;

    public MessageMapEntitiesViewModel(SelectedMessageModel model) {
        mEntityToMessageHashMap = new HashMap<>();
        mSelectedModel = model;
    }

    @Override
    public void onEntityClick(Entity entity) {
        mSelectedModel.select(mEntityToMessageHashMap.get(entity));
    }

    public Entity addMessage(Message message) {
        PointGeometry point = getPointGeometry(message);
        String type = getEntityType(message);

        Entity userEntity = createMessagePinEntity(point, type);
        mEntityToMessageHashMap.put(userEntity, message);
        userEntity.setOnClickListener(this);
        return userEntity;
    }

    private String getEntityType(Message message) {
        switch (message.getType()) {
            case Message.GEO:
                return ((MessageGeo) message).getContent().getType();
            case Message.IMAGE:
                return ((MessageImage) message).getType();
            default:
                throw new IllegalArgumentException("Message type added to map is not supported");
        }
    }

    private PointGeometry getPointGeometry(Message message) {
        switch (message.getType()) {
            case Message.GEO:
                return ((MessageGeo) message).getContent().getPointGeometry();
            case Message.IMAGE:
                return ((MessageImage) message).getContent().getLocation();
            default:
                throw new IllegalArgumentException("Message type added to map is not supported");
        }
    }

    private Entity createMessagePinEntity(PointGeometry pointGeometry, String type) {

//        //Todo: use symbol interface
//        PointImageSymbol pointSymbol;
//        if (type.equals(GeoTextSample.BUILDING)) {
//            pointSymbol= new PointImageSymbol(
//                    "Cesium/Assets/Textures/maki/building.png", 36,
//                    36);
//        }
//        else if (type.equals(GeoTextSample.ENEMY)) {
//            pointSymbol= new PointImageSymbol(
//                    "Cesium/Assets/Textures/maki/danger.png", 36,
//                    36);
//        }
//        else {
//
//        }
        //Todo: use symbol interface

        PointImageSymbol pointSymbol = new PointImageSymbol(
                "Cesium/Assets/Textures/maki/marker.png",
                36, 36);
        return new Point.Builder()
                .setGeometry(pointGeometry)
                .setSymbol(pointSymbol)
                .build();
    }
}

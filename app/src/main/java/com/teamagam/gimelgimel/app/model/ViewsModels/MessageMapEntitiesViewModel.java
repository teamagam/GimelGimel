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
        PointGeometry point;
        switch (message.getType()) {
            case Message.LAT_LONG:
                point = ((MessageLatLong) message).getContent();
                break;
            case Message.IMAGE:
                point = ((MessageImage) message).getContent().getLocation();
                break;
            default:
                throw new IllegalArgumentException("Message added to map should be supported type.");
        }

        Entity userEntity = createMessagePinEntity(point);
        mEntityToMessageHashMap.put(userEntity, message);
        userEntity.setOnClickListener(this);
        return userEntity;
    }

    private Entity createMessagePinEntity(PointGeometry pointGeometry) {
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
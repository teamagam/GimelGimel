package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.Context;

import com.teamagam.gimelgimel.R;
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
    private final Map<String, String> mEntityTypeToMarker;

    public MessageMapEntitiesViewModel(SelectedMessageModel model, Context context) {
        mEntityToMessageHashMap = new HashMap<>();
        mSelectedModel = model;
        mEntityTypeToMarker = new HashMap<>();

        initEntityMarkersMap(context);

    }

    private void initEntityMarkersMap(Context context) {
        String[] entityTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        String[] entityMarkers = context.getResources().getStringArray(R.array.geo_locations_markers_matched_types);

        for (int i = 0; i < entityTypes.length; i++) {
            mEntityTypeToMarker.put(entityTypes[i], entityMarkers[i]);
        }
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
        PointImageSymbol pointSymbol = new PointImageSymbol(
                mEntityTypeToMarker.get(type),
                36, 36);
        return new Point.Builder()
                .setGeometry(pointGeometry)
                .setSymbol(pointSymbol)
                .build();
    }
}

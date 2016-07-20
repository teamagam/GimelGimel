package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.fragments.ViewerFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds message pin locations.
 */
public class MessageMapEntitiesViewModel implements Entity.OnClickListener {

    private final SelectedMessageModel mSelectedModel;
    private Map<Entity, Message> mEntityToMessageHashMap;
    private ViewerFragment.MessageSymbolizer mSymbolizer;

    public MessageMapEntitiesViewModel(SelectedMessageModel model, ViewerFragment.MessageSymbolizer symbolizer) {
        mEntityToMessageHashMap = new HashMap<>();
        mSelectedModel = model;
        mSymbolizer = symbolizer;
    }

    @Override
    public void onEntityClick(Entity entity) {
        mSelectedModel.select(mEntityToMessageHashMap.get(entity));
    }

    public Entity addMessage(Message message) {
        PointGeometry point = getPointGeometry(message);
        Symbol symbol = mSymbolizer.symbolize(message);

        Entity userEntity = createMessagePinEntity(point, symbol);
        mEntityToMessageHashMap.put(userEntity, message);
        userEntity.setOnClickListener(this);
        return userEntity;
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

    private Entity createMessagePinEntity(PointGeometry pointGeometry, Symbol symbol) {
        return new Point.Builder()
                .setGeometry(pointGeometry)
                .setSymbol(symbol)
                .build();
    }
}

package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.support.annotation.NonNull;

import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.IMessageSymbolizer;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Holds message pin locations.
 */
@Singleton
public class MessageMapEntitiesViewModel implements Entity.OnClickListener {

    private Map<Entity, Message> mEntityToMessageHashMap;

    @Inject
    SelectedMessageModel mSelectedModel;

    @Inject
    IMessageSymbolizer mSymbolizer;

    public MessageMapEntitiesViewModel(SelectedMessageModel model, IMessageSymbolizer symbolizer) {
        mEntityToMessageHashMap = new HashMap<>();
        mSelectedModel = model;
        mSymbolizer = symbolizer;
    }

    @Override
    public void onEntityClick(Entity entity) {
        mSelectedModel.select(mEntityToMessageHashMap.get(entity));
    }

    public Entity addSentMessage(Message message) {
        return addMessage(message);
    }

    public Entity addReceivedMessage(Message message) {
        Entity entity = addMessage(message);
        mEntityToMessageHashMap.put(entity, message);
        entity.setOnClickListener(this);

        return entity;
    }

    @NonNull
    private Entity addMessage(Message message) {
        PointGeometry point = getPointGeometry(message);
        Symbol symbol = mSymbolizer.symbolize(message);

        return createMessagePinEntity(point, symbol);
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

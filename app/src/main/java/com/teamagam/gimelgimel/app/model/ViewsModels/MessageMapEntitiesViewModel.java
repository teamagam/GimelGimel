package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.support.annotation.NonNull;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.IMessageSymbolizer;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Holds message pin locations.
 */
@PerActivity
public class MessageMapEntitiesViewModel implements Entity.OnClickListener {

    private Map<Entity, MessageApp> mEntityToMessageHashMap;

//    @Inject
    SelectedMessageModel mSelectedModel;

    @Inject
    @Named("entitySymbolizer")
    IMessageSymbolizer mSymbolizer;

    @Inject
    public MessageMapEntitiesViewModel() {
        mEntityToMessageHashMap = new HashMap<>();
    }

    public MessageMapEntitiesViewModel(SelectedMessageModel model, IMessageSymbolizer symbolizer) {
        mEntityToMessageHashMap = new HashMap<>();
        mSelectedModel = model;
        mSymbolizer = symbolizer;
    }

    @Override
    public void onEntityClick(Entity entity) {
        mSelectedModel.select(mEntityToMessageHashMap.get(entity));
    }

    public Entity addSentMessage(MessageApp message) {
        return addMessage(message);
    }

    public Entity addReceivedMessage(MessageApp message) {
        Entity entity = addMessage(message);
        mEntityToMessageHashMap.put(entity, message);
        entity.setOnClickListener(this);

        return entity;
    }

    @NonNull
    private Entity addMessage(MessageApp message) {
        PointGeometryApp point = getPointGeometry(message);
        SymbolApp symbol = mSymbolizer.symbolize(message);

        return createMessagePinEntity(point, symbol);
    }

    private PointGeometryApp getPointGeometry(MessageApp message) {
        switch (message.getType()) {
            case MessageApp.GEO:
                return ((MessageGeoApp) message).getContent().getPointGeometry();
            case MessageApp.IMAGE:
                return ((MessageImageApp) message).getContent().getLocation();
            default:
                throw new IllegalArgumentException("MessageApp type added to map is not supported");
        }
    }

    private Entity createMessagePinEntity(PointGeometryApp pointGeometry, SymbolApp symbol) {
        return new Point.Builder()
                .setGeometry(pointGeometry)
                .setSymbol(symbol)
                .build();
    }
}

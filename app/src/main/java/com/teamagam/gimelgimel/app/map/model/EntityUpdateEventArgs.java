package com.teamagam.gimelgimel.app.map.model;

import android.support.annotation.IntDef;

import com.teamagam.gimelgimel.app.map.model.entities.Entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Bar on 01-Mar-16.
 * <p>
 * An event args data object used to describe a change made
 * to a {@link Entity}
 */
public class EntityUpdateEventArgs {

    //Event types
    public static final int LAYER_CHANGED_EVENT_TYPE_ADD = 1;
    public static final int LAYER_CHANGED_EVENT_TYPE_REMOVE = 2;
    public static final int LAYER_CHANGED_EVENT_TYPE_UPDATE = 3;
    public String layerId;
    public Entity entity;
    public
    @LayerChangedEventType
    int eventType;
    public EntityUpdateEventArgs(String layerId, Entity entity, @LayerChangedEventType int eventType) {
        this.layerId = layerId;
        this.entity = entity;
        this.eventType = eventType;
    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LAYER_CHANGED_EVENT_TYPE_ADD, LAYER_CHANGED_EVENT_TYPE_REMOVE, LAYER_CHANGED_EVENT_TYPE_UPDATE})
    public @interface LayerChangedEventType {
    }
}

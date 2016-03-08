package com.teamagam.gimelgimel.app.view.viewer.data;

import android.support.annotation.IntDef;

import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;

/**
 * Created by Bar on 01-Mar-16.
 *
 * An event args data object used to describe a change made
 * to a {@link VectorLayer}
 */
public class LayerChangedEventArgs {

    public String layerId;

    public Entity entity;

    public @LayerChangedEventType int eventType;

    public LayerChangedEventArgs(String layerId, Entity entity, @LayerChangedEventType int eventType){
        this.layerId = layerId;
        this.entity = entity;
        this.eventType = eventType;
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LAYER_CHANGED_EVENT_TYPE_ADD, LAYER_CHANGED_EVENT_TYPE_REMOVE, LAYER_CHANGED_EVENT_TYPE_UPDATE})
    public @interface LayerChangedEventType {}
    //Event types
    public static final int LAYER_CHANGED_EVENT_TYPE_ADD = 1;
    public static final int LAYER_CHANGED_EVENT_TYPE_REMOVE = 2;
    public static final int LAYER_CHANGED_EVENT_TYPE_UPDATE = 3;
}

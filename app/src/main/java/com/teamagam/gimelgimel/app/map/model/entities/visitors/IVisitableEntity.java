package com.teamagam.gimelgimel.app.map.model.entities.visitors;

/**
 * Created by Bar on 06-Mar-16.
 */
public interface IVisitableEntity {
    void accept(IEntitiesVisitor visitor);
}

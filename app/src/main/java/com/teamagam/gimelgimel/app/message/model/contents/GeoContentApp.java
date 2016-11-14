package com.teamagam.gimelgimel.app.message.model.contents;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentApp {

    private String mEntityId;

    public GeoContentApp(String entityId) {
        mEntityId = entityId;
    }

    public String getEntityId(){
        return mEntityId;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        s.append("entity id: ");
        s.append(mEntityId);
        s.append(']');
        return s.toString();
    }
}

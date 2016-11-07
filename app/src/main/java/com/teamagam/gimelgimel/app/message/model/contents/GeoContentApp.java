package com.teamagam.gimelgimel.app.message.model.contents;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentApp {

    private String mText;

    private String mEntityId;
    private String mLocationType;

    public GeoContentApp(String entityId, String text) {
        mEntityId = entityId;
        mText = text;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public String getEntityId(){
        return mEntityId;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        if (!mText.isEmpty()) {
            s.append("text=").append(mText);
        } else {
            s.append("text=?");
        }
        s.append("entity id: ");
        s.append(mEntityId);
        s.append(']');
        return s.toString();
    }
}

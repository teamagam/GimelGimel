package com.teamagam.gimelgimel.app.message.model.contents;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentApp implements Parcelable {

    private String mEntityId;

    public GeoContentApp(String entityId) {
        mEntityId = entityId;
    }

    protected GeoContentApp(Parcel in) {
        mEntityId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEntityId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeoContentApp> CREATOR = new Creator<GeoContentApp>() {
        @Override
        public GeoContentApp createFromParcel(Parcel in) {
            return new GeoContentApp(in);
        }

        @Override
        public GeoContentApp[] newArray(int size) {
            return new GeoContentApp[size];
        }
    };

    public String getEntityId() {
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

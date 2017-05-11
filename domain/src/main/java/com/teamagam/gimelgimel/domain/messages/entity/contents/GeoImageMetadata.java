package com.teamagam.gimelgimel.domain.messages.entity.contents;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

import java.util.Date;

public class GeoImageMetadata extends ImageMetadata {

    private GeoEntity mGeoEntity;

    public GeoImageMetadata(
            long time, String remoteUrl, String localUrl, String source, GeoEntity geoEntity) {
        super(time, remoteUrl, localUrl, source);
        mGeoEntity = geoEntity;

    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeoImageMetadata[");
        s.append(mSource);
        s.append(": ");
        if (mTime == 0) {
            s.append(" t=?!?");
        } else {
            s.append(" t=");
            s.append(new Date(mTime));
        }

        if (mRemoteUrl != null) {
            s.append(" url=");
            s.append(mRemoteUrl);
        }

        if (mGeoEntity != null) {
            s.append(" GeoEntity:");
            s.append(mGeoEntity);
        }

        s.append(']');
        return s.toString();
    }
}

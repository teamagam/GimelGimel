package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;

/**
 * Created by Yoni on 4/18/2016.
 * UserLocation-Type class for {@link Message}'s inner content
 */
public class MessageContentUserLocation implements MessageContentInterface{

    @SerializedName("locationSample")
    private LocationSample mSample;

    public MessageContentUserLocation(LocationSample sample) {
        mSample = sample;
    }

    public LocationSample getLocationSample() {
        return mSample;
    }

    public void setLocationSample(LocationSample point) {
        mSample = point;
    }
}

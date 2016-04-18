package com.teamagam.gimelgimel.app.model.entities;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Date;

/**
 * Created by Yoni on 4/18/2016.
 */
public class LocationSample {

//    --------MOCKING-------

    @SerializedName("location")
    private PointGeometry mPoint;

    @SerializedName("timeStamp")
    private Date mTime;

    @SerializedName("userName")
    private String mUserId;

    public LocationSample(PointGeometry point, Date time, String userId){
        mPoint = point;
        mTime = time;
        mUserId = userId;
    }

    //get + set
}

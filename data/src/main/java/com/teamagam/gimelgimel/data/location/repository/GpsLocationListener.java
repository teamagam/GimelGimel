package com.teamagam.gimelgimel.data.location.repository;


import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

public interface GpsLocationListener {

    void onNewLocation(LocationSample locationSample);

    void onBadConnection();
}

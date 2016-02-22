package com.teamagam.gimelgimel.app.model.rest;


import com.teamagam.gimelgimel.app.model.entities.FriendsEntity;

import java.util.List;

import retrofit.http.GET;

/***
 * An interface to describe the GG REST full API
 */

//todo: check if this is for entity or for db.
public interface GGApi {
    @GET("/friends")
    List<FriendsEntity> listFriends();
}

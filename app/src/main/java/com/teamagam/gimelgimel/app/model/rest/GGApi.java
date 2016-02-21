package com.teamagam.gimelgimel.app.model.rest;


import com.teamagam.gimelgimel.app.model.entities.FriendEntity;

import java.util.List;

import retrofit.http.GET;

/***
 * An interface to describe the GG REST full API
 */
public interface GGApi {
    @GET("/friends")
    List<FriendEntity> listFriends();
}

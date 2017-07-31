package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;

public interface IPolledMessagesProcessor {
  void process(ChatMessage polledMessage);

  void process(VectorLayer vectorLayer);

  void process(UserLocation userLocation);

  void process(DynamicLayer dynamicLayer);
}

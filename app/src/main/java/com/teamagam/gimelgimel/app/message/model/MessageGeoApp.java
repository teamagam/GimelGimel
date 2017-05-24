package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;

/**
 * A class for geo messages (using GeoContentData as its content)
 */
public class MessageGeoApp extends MessageApp<GeoContentApp> {

  public MessageGeoApp(GeoContentApp content) {
    super(MessageApp.GEO);
    mContent = content;
  }

  @Override
  public void accept(IMessageAppVisitor visitor) {
    visitor.visit(this);
  }
}



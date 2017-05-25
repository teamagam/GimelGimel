package com.teamagam.gimelgimel.app.message.model.visitor;

import com.teamagam.gimelgimel.app.message.model.MessageAlertApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.sensor.model.MessageSensorApp;

public interface IMessageAppVisitor {
  void visit(MessageGeoApp message);

  void visit(MessageTextApp message);

  void visit(MessageImageApp message);

  void visit(MessageSensorApp message);

  void visit(MessageAlertApp message);
}

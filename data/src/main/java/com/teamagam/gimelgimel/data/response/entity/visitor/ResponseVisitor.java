package com.teamagam.gimelgimel.data.response.entity.visitor;

import com.teamagam.gimelgimel.data.response.entity.AlertMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.UnknownResponse;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.response.entity.VectorLayerResponse;

public interface ResponseVisitor {

  void visit(UnknownResponse message);

  void visit(UserLocationResponse message);

  void visit(GeometryMessageResponse message);

  void visit(TextMessageResponse message);

  void visit(ImageMessageResponse message);

  void visit(VectorLayerResponse message);

  void visit(AlertMessageResponse message);
}

package com.teamagam.gimelgimel.data.response.entity.visitor;

import com.teamagam.gimelgimel.data.response.entity.AlertResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageResponse;
import com.teamagam.gimelgimel.data.response.entity.TextResponse;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.response.entity.VectorLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.UnknownResponse;

public interface IResponseVisitor {

  void visit(UnknownResponse message);

  void visit(UserLocationResponse message);

  void visit(GeometryResponse message);

  void visit(TextResponse message);

  void visit(ImageResponse message);

  void visit(VectorLayerResponse message);

  void visit(AlertResponse message);
}

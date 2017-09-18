package com.teamagam.gimelgimel.data.phases.server;

import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class PhasesResponse extends ServerResponse<PhasesData> {

  public PhasesResponse() {
    super(PHASES);
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}

package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.alerts.entity.AlertData;
import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitor;

public class AlertResponse extends GGResponse<AlertData> {

  public AlertResponse(AlertData content) {
    super(GGResponse.ALERT);
    mContent = content;
  }

  @Override
  public void accept(IResponseVisitor visitor) {
    visitor.visit(this);
  }
}

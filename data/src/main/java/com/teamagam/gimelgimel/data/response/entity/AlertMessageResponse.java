package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.alerts.entity.AlertData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class AlertMessageResponse extends ServerResponse<AlertData> {

  public AlertMessageResponse(AlertData content) {
    super(ServerResponse.ALERT);
    mContent = content;
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}

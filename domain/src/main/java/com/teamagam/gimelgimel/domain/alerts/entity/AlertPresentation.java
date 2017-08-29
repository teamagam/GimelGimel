package com.teamagam.gimelgimel.domain.alerts.entity;

public class AlertPresentation extends Alert {

  private String mText;

  public AlertPresentation(Alert alert, String text) {
    super(alert.getId(), alert.getSeverity(), alert.getSource(), alert.getTime(), alert.getType());
    mText = text;
  }

  public String getText() {
    return mText;
  }
}

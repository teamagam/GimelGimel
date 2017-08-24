package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;

public interface AlertMessageTextCreator {
  String createTextFromAlert(Alert alert, String text);
}

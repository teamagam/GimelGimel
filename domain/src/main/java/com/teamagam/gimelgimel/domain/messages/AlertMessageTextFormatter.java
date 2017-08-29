package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;

public interface AlertMessageTextFormatter {
  String format(Alert alert, String text);
}

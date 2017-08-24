package com.teamagam.gimelgimel.app.message;

import android.content.Context;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.AlertMessageTextCreator;
import javax.inject.Inject;

public class AlertMessageTextCreatorImpl implements AlertMessageTextCreator {

  private Context mContext;

  @Inject
  public AlertMessageTextCreatorImpl(Context context) {
    mContext = context;
  }

  @Override
  public String createTextFromAlert(Alert alert, String text) {
    if (alert.getType() == Alert.Type.VECTOR_LAYER) {
      return mContext.getString(R.string.vector_layer_alert_message_template, text);
    }
    return text;
  }
}

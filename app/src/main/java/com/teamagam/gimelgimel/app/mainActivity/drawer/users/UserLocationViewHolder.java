package com.teamagam.gimelgimel.app.mainActivity.drawer.users;

import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerViewHolder;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import java.util.Date;
import java.util.Locale;

public class UserLocationViewHolder extends BaseRecyclerViewHolder {

  private static final String LONG_DATE_FORMAT_SKELETON = "ddMMHHmm";

  private final String mLongDatePattern;
  private final java.text.DateFormat mTimeFormat;

  @BindView(R.id.drawer_user_item_name)
  TextView mNameText;
  @BindView(R.id.drawer_user_item_time)
  TextView mDateText;

  public UserLocationViewHolder(View itemView) {
    super(itemView);
    mTimeFormat = DateFormat.getTimeFormat(itemView.getContext());
    mLongDatePattern =
        DateFormat.getBestDateTimePattern(Locale.getDefault(), LONG_DATE_FORMAT_SKELETON);
  }

  public void bind(UserLocation userLocation) {
    mNameText.setText(userLocation.getUser());
    mDateText.setText(getDateText(userLocation.getLocationSample().getTime()));
    colorText(getTextColor(userLocation));
  }

  private String getDateText(long time) {
    Date date = new Date(time);
    if (DateUtils.isToday(time)) {
      return mTimeFormat.format(date);
    } else {
      return DateFormat.format(mLongDatePattern, time).toString();
    }
  }

  private void colorText(int color) {
    mNameText.setTextColor(color);
    mDateText.setTextColor(color);
  }

  private int getTextColor(UserLocation userLocation) {
    int colorResId = userLocation.isActive() ? R.color.active_user_color : R.color.stale_user_color;
    return ContextCompat.getColor(mAppContext, colorResId);
  }
}
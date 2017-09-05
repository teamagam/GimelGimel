package com.teamagam.gimelgimel.app.map.actions.timeplay;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.timeplay.DatePickerOpener;
import java.util.Calendar;

public class BetterPickersPicker
    implements DatePickerOpener, CalendarDatePickerDialogFragment.OnDateSetListener {

  private Context mContext;
  private FragmentManager mFragmentManager;
  private TimeplayViewModel.TextTimeDisplayer mTextTimeDisplayer;

  public BetterPickersPicker(Context context, FragmentManager fragmentManager) {
    mContext = context;
    mFragmentManager = fragmentManager;
  }

  public void setOnDateSetListener(TimeplayViewModel.TextTimeDisplayer textTimeDisplayer) {
    mTextTimeDisplayer = textTimeDisplayer;
  }

  @Override
  public void showPicker() {
    Calendar nowCalendar = Calendar.getInstance();
    CalendarDatePickerDialogFragment calendarDatePickerDialogFragment =
        new CalendarDatePickerDialogFragment().setOnDateSetListener(this)
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setPreselectedDate(nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH),
                nowCalendar.get(Calendar.DAY_OF_MONTH))
            .setDoneText(mContext.getString(R.string.done_label))
            .setCancelText(mContext.getString(R.string.picker_cancel));
    calendarDatePickerDialogFragment.show(mFragmentManager,
        mContext.getString(R.string.choose_date));
  }

  @Override
  public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int month, int day) {
    Calendar resultCalendar = Calendar.getInstance();
    resultCalendar.set(year, month, day);
    mTextTimeDisplayer.setResultCalender(resultCalendar);
    openTimePicker();
  }

  private void openTimePicker() {
    RadialTimePickerDialogFragment radialTimePickerDialogFragment =
        new RadialTimePickerDialogFragment().setOnTimeSetListener(mTextTimeDisplayer)
            .setStartTime(0, 0)
            .setDoneText(mContext.getString(R.string.done_label))
            .setCancelText(mContext.getString(R.string.picker_cancel));
    radialTimePickerDialogFragment.show(mFragmentManager,
        mContext.getString(R.string.choose_time_in_date));
  }

  public void showAlertErrorAndReopenPicker() {
    AlertDialog errorAlert = new AlertDialog.Builder(mContext).setTitle(R.string.error)
        .setMessage(R.string.timeplay_date_order_error)
        .setPositiveButton(R.string.reselect_date, (dialog, id) -> {
          dialog.cancel();
          showPicker();
        })
        .create();
    errorAlert.show();
  }
}

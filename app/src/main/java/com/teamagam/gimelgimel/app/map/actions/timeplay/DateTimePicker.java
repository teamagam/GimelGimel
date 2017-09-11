package com.teamagam.gimelgimel.app.map.actions.timeplay;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.teamagam.gimelgimel.R;
import java.util.Calendar;
import java.util.Date;

public class DateTimePicker implements TimeplayViewModel.DialogShower {

  private final int PRESELECTED_TIME_HOUR = 0;
  private final int PRESELECTED_TIME_MINUTE = 0;
  private Context mContext;
  private FragmentManager mFragmentManager;
  private TimeplayViewModel.TextTimeListener mTextTimeListener;

  public DateTimePicker(Context context, FragmentManager fragmentManager) {
    mContext = context;
    mFragmentManager = fragmentManager;
  }

  public void setOnDateSelectedListener(TimeplayViewModel.TextTimeListener textTimeListener) {
    mTextTimeListener = textTimeListener;
  }

  @Override
  public void show() {
    Calendar nowCalendar = Calendar.getInstance();
    CalendarDatePickerDialogFragment calendarDatePickerDialogFragment =
        new CalendarDatePickerDialogFragment().setOnDateSetListener((this::onDateSet))
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setPreselectedDate(nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH),
                nowCalendar.get(Calendar.DAY_OF_MONTH))
            .setDoneText(mContext.getString(R.string.confirm_button_text))
            .setCancelText(mContext.getString(R.string.picker_cancel));
    calendarDatePickerDialogFragment.show(mFragmentManager,
        mContext.getString(R.string.choose_date));
  }

  private void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int month, int day) {
    Calendar resultCalendar = Calendar.getInstance();
    resultCalendar.set(year, month, day);
    mTextTimeListener.setResultCalender(resultCalendar);
    openTimePicker();
  }

  private void openTimePicker() {
    RadialTimePickerDialogFragment radialTimePickerDialogFragment =
        new RadialTimePickerDialogFragment().setOnTimeSetListener(mTextTimeListener)
            .setStartTime(PRESELECTED_TIME_HOUR, PRESELECTED_TIME_MINUTE)
            .setDoneText(mContext.getString(R.string.confirm_button_text))
            .setCancelText(mContext.getString(R.string.picker_cancel));
    radialTimePickerDialogFragment.show(mFragmentManager, mContext.getString(R.string.choose_time));
  }

  public void showAlertErrorAndReopenPicker() {
    AlertDialog errorAlert = new AlertDialog.Builder(mContext).setTitle(R.string.error)
        .setMessage(R.string.timeplay_date_order_error)
        .setPositiveButton(R.string.reselect_date, (dialog, id) -> {
          dialog.cancel();
          show();
        })
        .create();
    errorAlert.show();
  }

  public interface DateDisplayer {
    void updateDate(Date newDate);

    boolean validateDate(long date);
  }
}

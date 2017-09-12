package com.teamagam.gimelgimel.app.map.actions.timeplay;

import android.databinding.Bindable;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.AutoTimeplayInteractorFactory;
import com.teamagam.gimelgimel.domain.timeplay.DatesRangeSnapshotTimeplayInteractorFactory;
import com.teamagam.gimelgimel.domain.timeplay.SnapshotTimeplayInteractorFactory;
import com.teamagam.gimelgimel.domain.timeplay.TimeplayInteractor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AutoFactory
public class TimeplayViewModel extends BaseViewModel {

  private static final int EARLIEST_TIMESTAMP = 0;
  private static final int AUTOPLAY_INTERVAL_COUNT_LOW_SPEED = 500;
  private static final int AUTOPLAY_INTERVAL_COUNT_MEDIUM_SPEED = 100;
  private static final int AUTOPLAY_INTERVAL_COUNT_HIGH_SPEED = 20;
  private static final int MIN_PROGRESS = 0;
  private final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

  private final AutoTimeplayInteractorFactory mAutoTimeplayInteractorFactory;
  private final SnapshotTimeplayInteractorFactory mSnapshotTimeplayInteractorFactory;
  private final DatesRangeSnapshotTimeplayInteractorFactory
      mDatesRangeSnapshotTimeplayInteractorFactory;
  private final MapDisplayer mMapDisplayer;
  private final DateFormat mDateFormat;
  private final DateFormat mTimeFormat;
  private final String mDateDefaultString;

  private TimeplayInteractor mDisplayInteractor;
  private TimeplayInteractor mSnapshotInteractor;

  private Date mCurrentDisplayedDate;
  private boolean mIsPlaying;
  private long mStartTimestamp;
  private long mEndTimestamp;
  private TimeplayDisplayer mTimeplayDisplayer;
  private boolean mIsSettingsPanelShown;
  private DateTimePicker mDateTimePickerOpener;
  private DateFormat mSettingsDateFormat;
  private PlaySpeed mPlaySpeed;

  public TimeplayViewModel(
      @Provided SnapshotTimeplayInteractorFactory snapshotTimeplayInteractorFactory,
      @Provided AutoTimeplayInteractorFactory datesRangeTimeplayInteractorFactory,
      @Provided
          DatesRangeSnapshotTimeplayInteractorFactory datesRangeSnapshotTimeplayInteractorFactory,
      DateFormat dateFormat,
      DateFormat timeFormat,
      String dateDefaultString,
      MapDisplayer mapDisplayer,
      DateTimePicker dateTimePickerOpener) {
    mAutoTimeplayInteractorFactory = datesRangeTimeplayInteractorFactory;
    mSnapshotTimeplayInteractorFactory = snapshotTimeplayInteractorFactory;
    mDatesRangeSnapshotTimeplayInteractorFactory = datesRangeSnapshotTimeplayInteractorFactory;
    mDateFormat = dateFormat;
    mTimeFormat = timeFormat;
    mMapDisplayer = mapDisplayer;
    mDateDefaultString = dateDefaultString;
    mDateTimePickerOpener = dateTimePickerOpener;
    mTimeplayDisplayer = new TimeplayDisplayer();
    mCurrentDisplayedDate = null;
    mIsPlaying = false;
    mStartTimestamp = -1;
    mEndTimestamp = -1;
    mIsSettingsPanelShown = false;
    mSettingsDateFormat = new SimpleDateFormat(DATE_FORMAT);
    mPlaySpeed = PlaySpeed.Medium;
  }

  public String getFormattedDate() {
    return format(mDateFormat, mDateDefaultString);
  }

  public String getFormattedTime() {
    return format(mTimeFormat, mDateDefaultString);
  }

  @Bindable
  public int getPlayOrResumeDrawableId() {
    return mIsPlaying ? R.drawable.ic_pause : R.drawable.ic_play;
  }

  public int getProgress() {
    return hasNotStarted() ? MIN_PROGRESS : getTimelineProgressPercentage();
  }

  public void onPlayResumeClicked() {
    if (mIsPlaying) {
      pause();
    } else {
      play();
    }
    notifyPropertyChanged(BR.settingsPanelShown);
  }

  public void onTimePlaySettingsPanelClicked() {
    mIsSettingsPanelShown = !mIsSettingsPanelShown;
    notifyPropertyChanged(BR.settingsPanelShown);
  }

  @Bindable
  public boolean isSettingsPanelShown() {
    return mIsSettingsPanelShown;
  }

  @Bindable
  public String getStartDateText() {
    return mStartTimestamp == -1 ? "" : mSettingsDateFormat.format(mStartTimestamp);
  }

  public void onStartDateClicked() {
    pause();
    mDateTimePickerOpener.setOnDateSelectedListener(new TextTimeListener(new StartDateDisplayer()));
    mDateTimePickerOpener.show();
  }

  @Bindable
  public String getEndDateText() {
    return mEndTimestamp == -1 ? "" : mSettingsDateFormat.format(mEndTimestamp);
  }

  public void onEndDateClicked() {
    pause();
    mDateTimePickerOpener.setOnDateSelectedListener(new TextTimeListener(new EndDateDisplayer()));
    mDateTimePickerOpener.show();
  }

  @Bindable
  public boolean isLowSpeedButtonChecked() {
    return mPlaySpeed == PlaySpeed.Low;
  }

  @Bindable
  public boolean isMediumSpeedButtonChecked() {
    return mPlaySpeed == PlaySpeed.Medium;
  }

  @Bindable
  public boolean isHighSpeedButtonChecked() {
    return mPlaySpeed == PlaySpeed.High;
  }

  public void setSpeedButtonChecked(PlaySpeed playSpeed) {
    mPlaySpeed = playSpeed;
    changePlaySpeed();
  }

  public void onProgressBarUserChange(double normalizedProgress) {
    pause();
    long newTimestamp = (long) (mStartTimestamp + normalizedProgress * getTotalTimespan());
    mCurrentDisplayedDate = new Date(newTimestamp);
    showSnapshot(newTimestamp);
    updateUi();
  }

  public void onMapReady() {
    showSnapshot(EARLIEST_TIMESTAMP);
  }

  private void changePlaySpeed() {
    if (mIsPlaying) {
      pause();
      play();
    }
  }

  private int getIntervalCount() {
    if (mPlaySpeed == PlaySpeed.Low) {
      return AUTOPLAY_INTERVAL_COUNT_LOW_SPEED;
    } else if (mPlaySpeed == PlaySpeed.Medium) {
      return AUTOPLAY_INTERVAL_COUNT_MEDIUM_SPEED;
    }
    return AUTOPLAY_INTERVAL_COUNT_HIGH_SPEED;
  }

  private String format(DateFormat dateFormat, String defaultValue) {
    return mCurrentDisplayedDate == null ? defaultValue : dateFormat.format(mCurrentDisplayedDate);
  }

  private boolean hasNotStarted() {
    return mCurrentDisplayedDate == null || mStartTimestamp == -1 || mEndTimestamp == -1;
  }

  private int getTimelineProgressPercentage() {
    long currentDelta = mCurrentDisplayedDate.getTime() - mStartTimestamp;
    double proportion = (currentDelta * 1.0) / getTotalTimespan();
    return (int) Math.ceil(proportion * 100);
  }

  private long getTotalTimespan() {
    return mEndTimestamp - mStartTimestamp;
  }

  private void pause() {
    unsubscribe(mDisplayInteractor);
    mIsPlaying = false;
    notifyPropertyChanged(BR.playOrResumeDrawableId);
  }

  private void play() {
    mTimeplayDisplayer.clearMap();
    mDisplayInteractor = createAdvancedSettingsInteractor();
    execute(mDisplayInteractor);
    mIsPlaying = true;
    notifyPropertyChanged(BR.playOrResumeDrawableId);
  }

  private TimeplayInteractor createAdvancedSettingsInteractor() {
    return mAutoTimeplayInteractorFactory.create(mStartTimestamp, mEndTimestamp, mTimeplayDisplayer,
        getIntervalCount(), getInitialTimestamp());
  }

  private long getInitialTimestamp() {
    return mCurrentDisplayedDate == null ? 0 : mCurrentDisplayedDate.getTime();
  }

  private void updateUi() {
    notifyPropertyChanged(BR._all);
  }

  private void showSnapshot(long newTimestamp) {
    mTimeplayDisplayer.clearMap();
    stopPreviousSnapshotDisplay();
    startCurrentSnapshotDisplay(newTimestamp);
  }

  private void stopPreviousSnapshotDisplay() {
    unsubscribe(mSnapshotInteractor);
  }

  private void startCurrentSnapshotDisplay(long newTimestamp) {
    mSnapshotInteractor = createSnapshotInteractor(newTimestamp);
    mSnapshotInteractor.execute();
  }

  private TimeplayInteractor createSnapshotInteractor(long newTimestamp) {
    return hasNotStarted() ? mSnapshotTimeplayInteractorFactory.create(mTimeplayDisplayer,
        newTimestamp)
        : mDatesRangeSnapshotTimeplayInteractorFactory.create(mStartTimestamp, mEndTimestamp,
            mTimeplayDisplayer, newTimestamp);
  }

  public enum PlaySpeed {
    Low,
    Medium,
    High
  }

  interface MapDisplayer {
    void addToMap(GeoEntity geoEntity);

    void removeFromMap(GeoEntity geoEntity);
  }

  public interface DateDisplayer {
    void updateDate(Date newDate);

    boolean validateDate(long date);
  }

  private class TimeplayDisplayer implements TimeplayInteractor.Displayer {

    private final Set<GeoEntity> mDisplayed = new HashSet<>();

    @Override
    public void displayTimestamp(long timestamp) {
      mCurrentDisplayedDate = new Date(timestamp);
      updateUi();
    }

    @Override
    public void setTimespan(long startTimestamp, long endTimestamp) {
      mStartTimestamp = startTimestamp;
      mEndTimestamp = endTimestamp;
    }

    @Override
    public void addToMap(GeoEntity geoEntity) {
      mMapDisplayer.addToMap(geoEntity);
      mDisplayed.add(geoEntity);
    }

    @Override
    public void removeFromMap(GeoEntity geoEntity) {
      mMapDisplayer.removeFromMap(geoEntity);
      mDisplayed.remove(geoEntity);
    }

    public void clearMap() {
      for (GeoEntity entity : mDisplayed) {
        mMapDisplayer.removeFromMap(entity);
      }
      mDisplayed.clear();
    }
  }

  private class StartDateDisplayer implements DateDisplayer {
    @Override
    public void updateDate(Date newDate) {
      mStartTimestamp = newDate.getTime();
      notifyPropertyChanged(BR.startDateText);
      showSnapshot(mStartTimestamp);
    }

    @Override
    public boolean validateDate(long newDate) {
      return mEndTimestamp == -1 || newDate < mEndTimestamp;
    }
  }

  private class EndDateDisplayer implements DateDisplayer {
    @Override
    public void updateDate(Date newDate) {
      mEndTimestamp = newDate.getTime();
      notifyPropertyChanged(BR.endDateText);
      showSnapshot(mStartTimestamp);
    }

    @Override
    public boolean validateDate(long newDate) {
      return mStartTimestamp == -1 || newDate > mStartTimestamp;
    }
  }

  public class TextTimeListener implements RadialTimePickerDialogFragment.OnTimeSetListener {

    private DateDisplayer mDateDisplayer;
    private Calendar mResultCalender;

    public TextTimeListener(DateDisplayer dateDisplayer) {
      mDateDisplayer = dateDisplayer;
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hour, int minute) {
      updateResultDateWithTime(hour, minute);
      if (mDateDisplayer.validateDate(mResultCalender.getTime().getTime())) {
        mDateDisplayer.updateDate(mResultCalender.getTime());
      } else {
        mDateTimePickerOpener.showAlertErrorAndReopenPicker();
      }
    }

    public void setResultCalender(Calendar resultCalender) {
      mResultCalender = resultCalender;
    }

    private void updateResultDateWithTime(int hour, int minute) {
      int year = mResultCalender.get(Calendar.YEAR);
      int month = mResultCalender.get(Calendar.MONTH);
      int day = mResultCalender.get(Calendar.DAY_OF_MONTH);
      mResultCalender.set(year, month, day, hour, minute);
    }
  }
}
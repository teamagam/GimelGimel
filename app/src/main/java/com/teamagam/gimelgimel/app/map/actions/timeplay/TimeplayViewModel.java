package com.teamagam.gimelgimel.app.map.actions.timeplay;

import android.databinding.Bindable;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.AdvancedSettingsRangeTimeplayInteractor;
import com.teamagam.gimelgimel.domain.timeplay.AdvancedSettingsRangeTimeplayInteractorFactory;
import com.teamagam.gimelgimel.domain.timeplay.AutoTimeplayInteractorFactory;
import com.teamagam.gimelgimel.domain.timeplay.SnapshotTimeplayInteractor;
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

  private final AdvancedSettingsRangeTimeplayInteractorFactory
      mAdvancedSettingsRangeTimeplayInteractorFactory;
  private final AutoTimeplayInteractorFactory mAutoTimeplayInteractorFactory;
  private final SnapshotTimeplayInteractorFactory mSnapshotTimeplayInteractorFactory;
  private final MapDisplayer mMapDisplayer;
  private final DateFormat mDateFormat;
  private final DateFormat mTimeFormat;
  private final String mDateDefaultString;

  private TimeplayInteractor mDisplayInteractor;
  private SnapshotTimeplayInteractor mSnapshotInteractor;

  private Date mCurrentDisplayedDate;
  private boolean mIsPlaying;
  private long mStartTimestamp;
  private long mEndTimestamp;
  private TimeplayDisplayer mTimeplayDisplayer;
  private boolean mIsSettingsPanelShown;

  private DateTimePicker mDateTimePickerOpener;
  private DateFormat mSettingsDateFormat;
  private PlaySpeed mPlaySpeed;

  public TimeplayViewModel(@Provided AutoTimeplayInteractorFactory autoTimeplayInteractorFactory,
      @Provided SnapshotTimeplayInteractorFactory snapshotTimeplayInteractorFactory,
      @Provided
          AdvancedSettingsRangeTimeplayInteractorFactory advancedSettingsRangeTimeplayInteractorFactory,
      DateFormat dateFormat,
      DateFormat timeFormat,
      String dateDefaultString,
      MapDisplayer mapDisplayer,
      DateTimePicker dateTimePickerOpener) {
    mAdvancedSettingsRangeTimeplayInteractorFactory =
        advancedSettingsRangeTimeplayInteractorFactory;
    mAutoTimeplayInteractorFactory = autoTimeplayInteractorFactory;
    mSnapshotTimeplayInteractorFactory = snapshotTimeplayInteractorFactory;
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

  public int getPlayOrResumeDrawableId() {
    return mIsPlaying ? R.drawable.ic_pause : R.drawable.ic_play;
  }

  public int getProgress() {
    if (hasNotStarted()) {
      return MIN_PROGRESS;
    }
    return getTimelineProgressPercentage();
  }

  public void onPlayResumeClicked() {
    if (mIsPlaying) {
      pause();
    } else {
      play();
    }
    updateUi();
  }

  public void onTimePlaySettingsPanelClicked() {
    mIsSettingsPanelShown = !mIsSettingsPanelShown;
    notifyPropertyChanged(BR.settingsPanelShown);
    if (!mIsSettingsPanelShown) {
      backToDefault();
    }
  }

  @Bindable
  public boolean isSettingsPanelShown() {
    return mIsSettingsPanelShown;
  }

  @Bindable
  public String getStartDateText() {
    resetStatusBarToStartAndPause();
    return mStartTimestamp == -1 ? "" : mSettingsDateFormat.format(mStartTimestamp);
  }

  public void onStartDateClicked() {
    mDateTimePickerOpener.setOnDateSelectedListener(new TextTimeListener(new StartDateDisplayer()));
    mDateTimePickerOpener.show();
  }

  @Bindable
  public String getEndDateText() {
    resetStatusBarToStartAndPause();
    return mEndTimestamp == -1 ? "" : mSettingsDateFormat.format(mEndTimestamp);
  }

  public void onEndDateClicked() {
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

  public void setLowSpeedButtonChecked() {
    mPlaySpeed = PlaySpeed.Low;
    speedUpdateLiveChange();
  }

  public void setMediumSpeedButtonChecked() {
    mPlaySpeed = PlaySpeed.Medium;
    speedUpdateLiveChange();
  }

  public void setHighSpeedButtonChecked() {
    mPlaySpeed = PlaySpeed.High;
    speedUpdateLiveChange();
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

  private void speedUpdateLiveChange() {
    if (mIsPlaying) {
      pause();
      play();
    }
  }

  private void resetStatusBarToStartAndPause() {
    pauseIfPlaying();
    mCurrentDisplayedDate = null;
    onMapReady();
  }

  private void pauseIfPlaying() {
    if (mIsPlaying) {
      pause();
    }
  }

  private int getCurrentPlaySpeed() {
    if (mPlaySpeed == PlaySpeed.Low) {
      return AUTOPLAY_INTERVAL_COUNT_LOW_SPEED;
    } else if (mPlaySpeed == PlaySpeed.Medium) {
      return AUTOPLAY_INTERVAL_COUNT_MEDIUM_SPEED;
    }
    return AUTOPLAY_INTERVAL_COUNT_HIGH_SPEED;
  }

  private String format(DateFormat dateFormat, String defaultValue) {
    if (mCurrentDisplayedDate == null) {
      return defaultValue;
    }
    return dateFormat.format(mCurrentDisplayedDate);
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
  }

  private void play() {
    mTimeplayDisplayer.clearMap();
    if (mIsSettingsPanelShown) {
      mDisplayInteractor = createAdvancedSettingsInteractor();
    } else {
      mDisplayInteractor = createAutoDisplayInteractor();
    }
    execute(mDisplayInteractor);
    mIsPlaying = true;
  }

  //// TODO: 10/09/2017: Do not use new Date().
  //// TODO: 10/09/2017: Make the settings panel to be above the map and not part of the screen.
  private TimeplayInteractor createAdvancedSettingsInteractor() {
    return mAdvancedSettingsRangeTimeplayInteractorFactory.create(
        new AdvancedSettingsRangeTimeplayInteractor.CustomDatesTimespan(new Date(mStartTimestamp),
            new Date(mEndTimestamp)), mTimeplayDisplayer, getCurrentPlaySpeed(),
        getInitialTimestamp());
  }

  private TimeplayInteractor createAutoDisplayInteractor() {
    return mAutoTimeplayInteractorFactory.create(mTimeplayDisplayer, getCurrentPlaySpeed(),
        getInitialTimestamp());
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
    if (mSnapshotInteractor != null) {
      mSnapshotInteractor.unsubscribe();
    }
  }

  private void startCurrentSnapshotDisplay(long newTimestamp) {
    mSnapshotInteractor =
        mSnapshotTimeplayInteractorFactory.create(mTimeplayDisplayer, newTimestamp);
    mSnapshotInteractor.execute();
  }

  private void backToDefault() {
    pauseIfPlaying();
    mCurrentDisplayedDate = null;
    mStartTimestamp = -1;
    mEndTimestamp = -1;
    mPlaySpeed = PlaySpeed.Medium;
    updateUi();
    onMapReady();
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

  private class StartDateDisplayer implements TimeplayInteractor.DateDisplayer {
    @Override
    public void updateDate(Date newDate) {
      mStartTimestamp = newDate.getTime();
      notifyPropertyChanged(BR.startDateText);
    }

    @Override
    public boolean validateDate(long newDate) {
      return mEndTimestamp == -1 || newDate < mEndTimestamp;
    }
  }

  private class EndDateDisplayer implements TimeplayInteractor.DateDisplayer {
    @Override
    public void updateDate(Date newDate) {
      mEndTimestamp = newDate.getTime();
      notifyPropertyChanged(BR.endDateText);
    }

    @Override
    public boolean validateDate(long newDate) {
      return mStartTimestamp == -1 || newDate > mStartTimestamp;
    }
  }

  public class TextTimeListener implements RadialTimePickerDialogFragment.OnTimeSetListener {

    private TimeplayInteractor.DateDisplayer mDateDisplayer;
    private Calendar mResultCalender;

    public TextTimeListener(TimeplayInteractor.DateDisplayer dateDisplayer) {
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
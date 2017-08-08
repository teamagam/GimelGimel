package com.teamagam.gimelgimel.app.map.actions.timeplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.timeplay.TimeplayInteractor;
import javax.inject.Inject;

public class TimeplayActionFragment extends BaseDrawActionFragment<TimeplayViewModel> {

  @BindView(R.id.timeplay_map)
  GGMapView mGGMapView;

  @Inject
  TimeplayViewModelFactory mTimeplayViewModelFactory;

  private TimeplayViewModel mTimeplayViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    mApp.getApplicationComponent().inject(this);
    mTimeplayViewModel = mTimeplayViewModelFactory.create(new TimelineDisplayer());

    return view;
  }

  @Override
  protected TimeplayViewModel getSpecificViewModel() {
    return mTimeplayViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.menu_action_timeplay_title);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_timeplay_action;
  }

  private class TimelineDisplayer implements TimeplayInteractor.Displayer {
    @Override
    public void addToMap(GeoEntity geoEntity) {
      mGGMapView.updateMapEntity(GeoEntityNotification.createAdd(geoEntity));
    }

    @Override
    public void removeFromMap(GeoEntity geoEntity) {
      mGGMapView.updateMapEntity(GeoEntityNotification.createRemove(geoEntity));
    }
  }
}

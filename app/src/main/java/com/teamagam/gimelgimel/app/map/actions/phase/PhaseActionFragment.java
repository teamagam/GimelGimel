package com.teamagam.gimelgimel.app.map.actions.phase;

import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;

public class PhaseActionFragment extends BaseDrawActionFragment {

  @BindView(R.id.phase_view_pager)
  ViewPager mViewPager;

  @BindView(R.id.phase_pager)
  PagerTabStrip mPagerTabStrip;

  private PhaseViewModel mPhaseViewModel;

  @Override
  protected BaseViewModel getSpecificViewModel() {
    return mPhaseViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.phase_action_title);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_phase;
  }
}

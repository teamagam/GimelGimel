package com.teamagam.gimelgimel.app.map.actions.phase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;

public class PhaseActionFragment extends BaseDrawActionFragment {

  private static final String PHASE_LAYER_ID_KEY = "phase_layer_id";

  @BindView(R.id.phase_view_pager)
  ViewPager mViewPager;
  @BindView(R.id.phase_pager)
  PagerTabStrip mPagerTabStrip;

  private PhaseViewModel mViewModel;

  public static PhaseActionFragment createFragment(String phaseLayerId) {
    Bundle bundle = new Bundle();
    bundle.putString(PHASE_LAYER_ID_KEY, phaseLayerId);
    PhaseActionFragment fragment = new PhaseActionFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mViewPager.setAdapter(new PhasesPagerAdapter(getChildFragmentManager()));

    String phaseLayerId = getArguments().getString(PHASE_LAYER_ID_KEY);
    mViewModel = null;
  }

  @Override
  protected BaseViewModel getSpecificViewModel() {
    return mViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.phase_action_title);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_phase;
  }

  private class PhasesPagerAdapter extends FragmentStatePagerAdapter {

    public PhasesPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
      return 0;
    }
  }
}

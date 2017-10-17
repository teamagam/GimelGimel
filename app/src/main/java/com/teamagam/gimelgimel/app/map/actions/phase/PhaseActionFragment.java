package com.teamagam.gimelgimel.app.map.actions.phase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.dynamic_layer.DynamicLayerDetailsFragment;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class PhaseActionFragment extends BaseDrawActionFragment
    implements DynamicLayerDetailsFragment.OnDynamicEntityClickedListener {

  private static final String PHASE_LAYER_ID_KEY = "phase_layer_id";

  @BindView(R.id.phase_map)
  GGMapView mGGMapView;

  @BindView(R.id.phase_view_pager)
  ViewPager mViewPager;
  @BindView(R.id.phase_pager)
  PagerTabStrip mPagerTabStrip;

  @Inject
  PhaseViewModelFactory mPhaseViewModelFactory;

  private PhaseViewModel mViewModel;
  private DelegatePhaseSelectionListener mPhaseSelectionListener;
  private PhasesPagerAdapter mPhasesPagerAdapter;

  public static PhaseActionFragment newInstance(String phaseLayerId) {
    Bundle bundle = new Bundle();
    bundle.putString(PHASE_LAYER_ID_KEY, phaseLayerId);
    PhaseActionFragment fragment = new PhaseActionFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((GGApplication) getContext().getApplicationContext()).getApplicationComponent().inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    initializePager();
    initializeViewModel();

    return view;
  }

  @Override
  public void onDynamicEntityListingClicked(DynamicEntity dynamicEntity) {
    mViewModel.onPhaseEntityClicked(dynamicEntity);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mViewPager.removeOnPageChangeListener(mPhaseSelectionListener);
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

  private void initializePager() {
    mPhasesPagerAdapter = new PhasesPagerAdapter(getChildFragmentManager());
    mViewPager.setAdapter(mPhasesPagerAdapter);
    mPhaseSelectionListener = new DelegatePhaseSelectionListener();
    mViewPager.addOnPageChangeListener(mPhaseSelectionListener);
  }

  private void initializeViewModel() {
    mViewModel = mPhaseViewModelFactory.create(mGGMapView,
        new BottomFragmentPanelPhasesDisplayer(mPhasesPagerAdapter), getBundledLayerId());
  }

  private String getBundledLayerId() {
    return getArguments().getString(PHASE_LAYER_ID_KEY);
  }

  private class BottomFragmentPanelPhasesDisplayer implements PhaseViewModel.PhasesDisplayer {
    private final PhasesPagerAdapter mPhasesPagerAdapter;

    BottomFragmentPanelPhasesDisplayer(PhasesPagerAdapter phasesPagerAdapter) {
      mPhasesPagerAdapter = phasesPagerAdapter;
    }

    @Override
    public void addPhase(DynamicLayer phase, int position) {
      mPhasesPagerAdapter.addPhase(phase, position);
      mPhasesPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayPhase(int position) {
      mViewPager.setCurrentItem(position);
    }
  }

  private class PhasesPagerAdapter extends FragmentStatePagerAdapter {

    private final List<DynamicLayer> mPhases;

    PhasesPagerAdapter(FragmentManager fm) {
      super(fm);
      mPhases = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
      return DynamicLayerDetailsFragment.newInstance(getPhase(position).getId());
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return getPhase(position).getName();
    }

    @Override
    public int getCount() {
      return mPhases.size();
    }

    public void addPhase(DynamicLayer phase, int position) {
      putAt(phase, position);
    }

    private DynamicLayer getPhase(int position) {
      return mPhases.get(position);
    }

    private void putAt(DynamicLayer phase, int position) {
      if (position < mPhases.size()) {
        mPhases.set(position, phase);
      } else {
        mPhases.add(position, phase);
      }
    }
  }

  private class DelegatePhaseSelectionListener extends ViewPager.SimpleOnPageChangeListener {
    @Override
    public void onPageSelected(int position) {
      super.onPageSelected(position);
      mViewModel.onPhaseSelected(position);
    }
  }
}

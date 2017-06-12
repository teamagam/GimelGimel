package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.databinding.ActivityMainDrawerBinding;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import devlight.io.library.ntb.NavigationTabBar;
import javax.inject.Inject;

public class MainActivityDrawer extends ActivitySubcomponent {

  @Inject
  DrawerViewModelFactory mDrawerViewModelFactory;
  @Inject
  UserPreferencesRepository mUserPreferencesRepository;

  @BindView(R.id.drawer_navigation_tab_bar)
  NavigationTabBar mNavigationTabBar;

  @BindView(R.id.drawer_content_layout)
  LinearLayout mDrawerContentLayout;

  @BindView(R.id.drawer_content_recycler)
  RecyclerView mRecyclerView;

  private DrawerViewModel mViewModel;
  private MainActivity mMainActivity;

  public MainActivityDrawer(MainActivity activity) {
    mMainActivity = activity;
    mMainActivity.getMainActivityComponent().inject(this);
    mMainActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ButterKnife.bind(this, activity);

    initViewModel();

    setupNavTabBar();

    mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));
  }

  private void initViewModel() {
    mViewModel = mDrawerViewModelFactory.create(mMainActivity, this::setRecyclerAdapter);
    bindViewModel();
    mViewModel.setView(this);
    mViewModel.start();
  }

  private void bindViewModel() {
    ActivityMainDrawerBinding binding = ActivityMainDrawerBinding.bind(mDrawerContentLayout);
    binding.setViewModel(mViewModel);
  }

  private void setupNavTabBar() {
    mNavigationTabBar.setModels(mViewModel.getTabModels());
    mNavigationTabBar.setOnTabBarSelectedIndexListener(
        new NavigationTabBar.OnTabBarSelectedIndexListener() {
          @Override
          public void onStartTabSelected(NavigationTabBar.Model model, int index) {
            mViewModel.onNavigationTabBarSelected(index);
          }

          @Override
          public void onEndTabSelected(NavigationTabBar.Model model, int index) {

          }
        });
    mNavigationTabBar.setModelIndex(0);
  }

  @Override
  public void onResume() {
    super.onResume();
    mViewModel.resume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mViewModel.pause();
  }

  private void setRecyclerAdapter(RecyclerView.Adapter adapter) {
    mRecyclerView.setAdapter(adapter);
  }
}

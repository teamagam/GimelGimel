package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.DrawerViewModel;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.DrawerViewModelFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import devlight.io.library.ntb.NavigationTabBar;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MainActivityDrawer extends ActivitySubcomponent {

  @Inject
  DrawerViewModelFactory mDrawerViewModelFactory;
  @Inject
  UserPreferencesRepository mUserPreferencesRepository;

  @BindView(R.id.drawer_navigation_tab_bar)
  NavigationTabBar mNavigationTabBar;

  @BindView(R.id.main_activity_drawer_layout)
  DrawerLayout mDrawerLayout;

  private DrawerViewModel mViewModel;
  private MainActivity mMainActivity;

  MainActivityDrawer(Activity activity) {
    mMainActivity = (MainActivity) activity;
    mMainActivity.getMainActivityComponent().inject(this);
    mMainActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ButterKnife.bind(this, activity);

    setupNavTabBar();

    //mViewModel = mDrawerViewModelFactory.create(mNavigationView, mDrawerLayout);
    //initViewModel();
  }

  private void setupNavTabBar() {
    mNavigationTabBar.setModels(createModels());
  }

  private List<NavigationTabBar.Model> createModels() {
    List<NavigationTabBar.Model> models = new ArrayList<>(4);

    models.add(
        createModel(R.string.drawer_nav_bar_users_title, R.drawable.ic_user, R.color.themeRed));
    models.add(createModel(R.string.drawer_nav_bar_bubble_layers_title, R.drawable.ic_bubbles,
        R.color.themeYellow));
    models.add(
        createModel(R.string.drawer_nav_bar_layers_title, R.drawable.ic_layers, R.color.themeBlue));
    models.add(
        createModel(R.string.drawer_nav_bar_rasters_title, R.drawable.ic_map, R.color.themePink));

    return models;
  }

  private NavigationTabBar.Model createModel(int titleId, int drawableId, int colorId) {
    return new NavigationTabBar.Model.Builder(getDrawable(drawableId), getColor(colorId)).title(
        getString(titleId)).build();
  }

  private Drawable getDrawable(int drawableId) {
    return ContextCompat.getDrawable(mMainActivity, drawableId);
  }

  private int getColor(int colorId) {
    return ContextCompat.getColor(mMainActivity, colorId);
  }

  private String getString(int titleId) {
    return mMainActivity.getString(titleId);
  }

  private void initViewModel() {
    mViewModel.setView(this);
    mViewModel.start();
  }

  @Override
  public void onResume() {
    super.onResume();
    //mViewModel.resume();
  }

  @Override
  public void onPause() {
    super.onPause();
    //mViewModel.pause();
  }

  public void addToMenu(int submenuId, int itemId, String title) {
    //SubMenu subMenu = mNavigationViewMenu.findItem(submenuId).getSubMenu();
    //subMenu.add(submenuId, itemId, Menu.NONE, title);
  }

  public void updateMenu(String title, int itemId) {
    //mNavigationViewMenu.findItem(itemId).setTitle(title);
  }

  public void setChecked(boolean isVisible, int itemId) {
    //mNavigationViewMenu.findItem(itemId).setChecked(isVisible);
  }

  public void setNavHeaderText(View drawerView) {
    TextView navHeaderText = (TextView) drawerView.findViewById(R.id.nav_header_text);
    String username = mUserPreferencesRepository.getString(
        mMainActivity.getResources().getString(R.string.user_name_text_key));
    navHeaderText.setText(username);
  }
}

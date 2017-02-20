package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.DrawerViewModel;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.DrawerViewModelFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivityDrawer extends ActivitySubcomponent {

    @Inject
    DrawerViewModelFactory mDrawerViewModelFactory;
    @Inject
    UserPreferencesRepository mUserPreferencesRepository;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout mDrawerLayout;
    private DrawerViewModel mViewModel;
    private MainActivity mMainActivity;
    private Menu mNavigationViewMenu;

    MainActivityDrawer(Activity activity) {
        mMainActivity = (MainActivity) activity;
        mMainActivity.getMainActivityComponent().inject(this);
        mMainActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this, activity);
        mNavigationViewMenu = mNavigationView.getMenu();
        mViewModel = mDrawerViewModelFactory.create(mNavigationView, mDrawerLayout);
        initViewModel();
    }

    private void initViewModel() {
        mViewModel.setView(this);
        mViewModel.start();
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

    public void addToMenu(String title, int id) {
        mNavigationViewMenu.add(R.id.drawer_menu_layers_group, id, 0, title);
    }

    public void updateMenu(String title, int id) {
        mNavigationViewMenu.findItem(id).setTitle(title);
    }

    public void setChecked(boolean isVisible, int id) {
        mNavigationViewMenu.findItem(id).setChecked(isVisible);
    }

    public void setNavHeaderText(View drawerView) {
        TextView navHeaderText = (TextView) drawerView.findViewById(R.id.nav_header_text);
        String username = mUserPreferencesRepository.getString(
                mMainActivity.getResources().getString(R.string.user_name_text_key));
        navHeaderText.setText(username);
    }
}

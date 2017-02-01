package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.utils.StringToIntegerMapper;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityDrawer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SetVectorLayerVisibilityInteractorFactory;

import javax.inject.Inject;

@PerActivity
public class DrawerViewModel extends BaseViewModel
        implements DisplayVectorLayersInteractor.Displayer {

    @Inject
    DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
    @Inject
    SetVectorLayerVisibilityInteractorFactory mSetVectorLayerVisibilityInteractorFactory;

    private DrawerStateListener mDrawerStateListener;

    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;

    private DrawerLayout mDrawerLayout;

    private StringToIntegerMapper mStringToIntegerMapper;

    @Inject
    public DrawerViewModel() {
    }

    public void start() {
        mStringToIntegerMapper = new StringToIntegerMapper();
        mDrawerStateListener = new DrawerStateListener();

        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(this);
    }

    public void setDrawerMenuSelectedListenerTo(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new DrawerItemSelectedListener());
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    @Override
    public void displayShown(VectorLayerPresentation vectorLayerPresentation) {
        display(vectorLayerPresentation, true);
    }

    @Override
    public void displayHidden(VectorLayerPresentation vectorLayerPresentation) {
        display(vectorLayerPresentation, false);
    }

    private void display(VectorLayerPresentation vectorLayerPresentation, boolean isVisible) {
        if (!mStringToIntegerMapper.contains(vectorLayerPresentation.getId())) {
            int intId = mStringToIntegerMapper.put(vectorLayerPresentation.getId());
            ((MainActivityDrawer) mView).addToMenu(vectorLayerPresentation.getName(), intId);
        }
        ((MainActivityDrawer) mView).setChecked(
                mStringToIntegerMapper.get(vectorLayerPresentation.getId()), isVisible);
    }

    public void resume() {
        mDisplayVectorLayersInteractor.execute();
        mDrawerLayout.addDrawerListener(mDrawerStateListener);
    }

    public void pause() {
        mDisplayVectorLayersInteractor.unsubscribe();
        mDrawerLayout.removeDrawerListener(mDrawerStateListener);
    }

    private class DrawerStateListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            sLogger.userInteraction("Drawer opened");

            ((MainActivityDrawer) mView).setNavHeaderText(drawerView);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            sLogger.userInteraction("Drawer closed");
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }

    private class DrawerItemSelectedListener
            implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            sLogger.userInteraction("Drawer item " + item + " clicked");
            switch (item.getGroupId()) {
                case R.id.drawer_menu_layers_group:
                    mSetVectorLayerVisibilityInteractorFactory.create(
                            mStringToIntegerMapper.get(item.getItemId()), !item.isChecked()
                    ).execute();
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}

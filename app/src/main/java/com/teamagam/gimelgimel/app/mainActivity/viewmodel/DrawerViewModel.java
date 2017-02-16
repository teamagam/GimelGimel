package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.utils.StringToIntegerMapper;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityDrawer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SetVectorLayerVisibilityInteractorFactory;

import javax.inject.Inject;

@AutoFactory
public class DrawerViewModel extends BaseViewModel<MainActivityDrawer> {

    private final StringToIntegerMapper mStringToIntegerMapper;
    @Inject
    DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
    @Inject
    SetVectorLayerVisibilityInteractorFactory mSetVectorLayerVisibilityInteractorFactory;
    private DrawerStateListener mDrawerStateListener;
    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private DrawerLayout mDrawerLayout;

    @Inject
    public DrawerViewModel(@Provided DisplayVectorLayersInteractorFactory
                                   displayVectorLayersInteractorFactory,
                           @Provided SetVectorLayerVisibilityInteractorFactory
                                   setVectorLayerVisibilityInteractorFactory,
                           NavigationView navigationView,
                           DrawerLayout drawerLayout) {
        mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
        mSetVectorLayerVisibilityInteractorFactory = setVectorLayerVisibilityInteractorFactory;
        navigationView.setNavigationItemSelectedListener(
                new DrawerItemSelectedListener());
        mDrawerLayout = drawerLayout;
        mStringToIntegerMapper = new StringToIntegerMapper();
        mDrawerStateListener = new DrawerStateListener();
    }

    @Override
    public void start() {
        super.start();
        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(
                new DrawerVectorLayersDisplayer());
    }

    public void resume() {
        mDisplayVectorLayersInteractor.execute();
        mDrawerLayout.addDrawerListener(mDrawerStateListener);
    }

    public void pause() {
        mDisplayVectorLayersInteractor.unsubscribe();
        mDrawerLayout.removeDrawerListener(mDrawerStateListener);
    }

    private int getMenuItemId(VectorLayerPresentation vectorLayerPresentation) {
        return mStringToIntegerMapper.get(vectorLayerPresentation.getId());
    }

    private String getVectorLayerId(@NonNull MenuItem item) {
        return mStringToIntegerMapper.get(item.getItemId());
    }

    private class DrawerVectorLayersDisplayer implements DisplayVectorLayersInteractor.Displayer {
        @Override
        public void display(VectorLayerPresentation vectorLayerPresentation) {
            addToMenuIfNecessary(vectorLayerPresentation);
            setChecked(vectorLayerPresentation);
        }

        private void addToMenuIfNecessary(VectorLayerPresentation vectorLayerPresentation) {
            if (!mStringToIntegerMapper.contains(vectorLayerPresentation.getId())) {
                int intId = mStringToIntegerMapper.put(vectorLayerPresentation.getId());
                mView.addToMenu(vectorLayerPresentation.getName(), intId);
            }
        }

        private void setChecked(VectorLayerPresentation vectorLayerPresentation) {
            mView.setChecked(getMenuItemId(vectorLayerPresentation),
                    vectorLayerPresentation.isShown());
        }
    }

    private class DrawerStateListener implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            sLogger.userInteraction("Drawer opened");
            mView.setNavHeaderText(drawerView);
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
                    onDrawerVectorLayerClicked(item);
                    break;
                default:
                    break;
            }
            return true;
        }

        private void onDrawerVectorLayerClicked(@NonNull MenuItem item) {
            mSetVectorLayerVisibilityInteractorFactory.create(
                    getVectorLayerId(item), !item.isChecked()
            ).execute();
        }
    }
}

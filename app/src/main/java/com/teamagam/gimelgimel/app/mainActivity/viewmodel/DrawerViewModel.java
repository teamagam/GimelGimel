package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.utils.IntegerIdGenerator;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityDrawer;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.SetVectorLayerVisibilityInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.SetVectorLayerVisibilityInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.SetIntermediateRasterInteractorFactory;

@AutoFactory
public class DrawerViewModel extends BaseViewModel<MainActivityDrawer> {

    private final IntegerIdGenerator mIntegerIdGenerator;

    private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
    private final SetVectorLayerVisibilityInteractorFactory mSetVectorLayerVisibilityInteractorFactory;

    private final DisplayIntermediateRastersInteractorFactory mDisplayIntermediateRastersInteractorFactory;
    private final SetIntermediateRasterInteractorFactory mSetIntermediateRasterInteractorFactory;

    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;

    private MenuHandler mMenuHandler;

    private DrawerStateListener mDrawerStateListener;
    private DrawerLayout mDrawerLayout;

    public DrawerViewModel(@Provided DisplayVectorLayersInteractorFactory
                                   displayVectorLayersInteractorFactory,
                           @Provided SetVectorLayerVisibilityInteractorFactory
                                   setVectorLayerVisibilityInteractorFactory,
                           @Provided DisplayIntermediateRastersInteractorFactory
                                   displayIntermediateRastersInteractorFactory,
                           @Provided SetIntermediateRasterInteractorFactory
                                   setIntermediateRasterInteractorFactory,
                           NavigationView navigationView,
                           DrawerLayout drawerLayout) {
        mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
        mSetVectorLayerVisibilityInteractorFactory = setVectorLayerVisibilityInteractorFactory;
        mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
        mSetIntermediateRasterInteractorFactory = setIntermediateRasterInteractorFactory;
        navigationView.setNavigationItemSelectedListener(
                new DrawerItemSelectedListener());
        mDrawerLayout = drawerLayout;
        mIntegerIdGenerator = new IntegerIdGenerator();
        mDrawerStateListener = new DrawerStateListener();
    }

    @Override
    public void start() {
        super.start();
        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(
                new DrawerVectorLayersDisplayer());
        mDisplayIntermediateRastersInteractor = mDisplayIntermediateRastersInteractorFactory.create(
                new IntermediateRasterDisplayer());
        mMenuHandler = new MenuHandler(mView, mIntegerIdGenerator);
    }

    public void resume() {
        mDisplayVectorLayersInteractor.execute();
        mDisplayIntermediateRastersInteractor.execute();
        mDrawerLayout.addDrawerListener(mDrawerStateListener);
    }

    public void pause() {
        mDisplayVectorLayersInteractor.unsubscribe();
        mDisplayIntermediateRastersInteractor.unsubscribe();
        mDrawerLayout.removeDrawerListener(mDrawerStateListener);
    }


    private class IntermediateRasterDisplayer
            implements DisplayIntermediateRastersInteractor.Displayer {
        @Override
        public void display(DisplayIntermediateRastersInteractor.IntermediateRasterPresentation
                                    intermediateRasterPresentation) {
            DrawerViewModel.this.display(
                    intermediateRasterPresentation.getName(),
                    intermediateRasterPresentation.getName(),
                    intermediateRasterPresentation.isShown(),
                    R.id.drawer_menu_submenu_rasters);
        }
    }

    private void display(String id, String title, boolean isShown, int submenuId) {
        applyMenuItemChange(id, title, submenuId);
        setChecked(id, isShown);
    }

    private void applyMenuItemChange(String id, String title, int submenuId) {
        if (mMenuHandler.isNew(id)) {
            mMenuHandler.add(id, title, submenuId);
        } else {
            mMenuHandler.update(id, title);
        }
    }

    private void setChecked(String rasterId, boolean isShown) {
        mMenuHandler.setChecked(rasterId, isShown);
    }

    private class DrawerVectorLayersDisplayer implements DisplayVectorLayersInteractor.Displayer {
        @Override
        public void display(VectorLayerPresentation vlp) {
            DrawerViewModel.this.display(
                    vlp.getId(),
                    vlp.getName(),
                    vlp.isShown(),
                    getSubmenuId(vlp)
            );
        }

        private int getSubmenuId(VectorLayerPresentation vlp) {
            if (vlp.getCategory() == VectorLayer.Category.FIRST) {
                return R.id.drawer_menu_submenu_bubble_layers;
            } else {
                return R.id.drawer_menu_submenu_layers;
            }
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
        public boolean onNavigationItemSelected(MenuItem item) {
            sLogger.userInteraction("Drawer item " + item + " clicked");
            switch (item.getGroupId()) {
                case R.id.drawer_menu_submenu_bubble_layers:
                case R.id.drawer_menu_submenu_layers:
                    onDrawerVectorLayerClicked(item);
                    break;
                case R.id.drawer_menu_submenu_rasters:
                    onDrawerRasterClicked(item);
                default:
                    break;
            }
            return true;
        }

        private void onDrawerVectorLayerClicked(MenuItem item) {
            VectorLayerVisibilityChange change =
                    new VectorLayerVisibilityChange(getVectorLayerId(item), isCheckedAfterToggle(item));
            mSetVectorLayerVisibilityInteractorFactory
                    .create(change)
                    .execute();
        }

        private String getVectorLayerId(int itemId) {
            return mIntegerIdGenerator.get(itemId);
        }

        private void onDrawerRasterClicked(MenuItem item) {
            if (isCheckedAfterToggle(item)) {
                mSetIntermediateRasterInteractorFactory.create(
                        getIntermediateRasterName(item.getItemId())).execute();
            } else {
                mSetIntermediateRasterInteractorFactory.create(null).execute();
            }
        }

        private String getIntermediateRasterName(int itemId) {
            return mIntegerIdGenerator.get(itemId);
        }

        private boolean isCheckedAfterToggle(MenuItem item) {
            return !item.isChecked();
        }
    }
}

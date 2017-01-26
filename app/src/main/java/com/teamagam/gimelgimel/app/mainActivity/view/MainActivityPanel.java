package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BottomPanelPagerAdapter;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.PanelViewModel;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.PanelViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityPanel extends ActivitySubcomponent {

    private static final AppLogger sLogger = AppLoggerFactory.create();

    @Inject
    PanelViewModelFactory mPanelViewModelFactory;

    @BindView(R.id.bottom_swiping_panel)
    ViewPager mBottomViewPager;

    @BindView(R.id.bottom_panel_tabs)
    PagerSlidingTabStrip mTabsStrip;

    @BindView(R.id.activity_main_layout)
    SlidingUpPanelLayout mSlidingLayout;

    private PanelViewModel mViewModel;
    private SlidingPanelListener mPanelListener;
    private PageChangeListener mPageListener;

    MainActivityPanel(FragmentManager fm, Activity activity) {
        ButterKnife.bind(this, activity);
        ((MainActivity) activity).getMainActivityComponent().inject(this);
        mViewModel = mPanelViewModelFactory.create(fm, activity);
        mViewModel.setView(this);
        mViewModel.start();

        mTabsStrip.setViewPager(mBottomViewPager);
        mPanelListener = new SlidingPanelListener();
        mPageListener = new PageChangeListener();
    }

    public void setAdapter(BottomPanelPagerAdapter pageAdapter) {
        mBottomViewPager.setAdapter(pageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSlidingLayout.addPanelSlideListener(mPanelListener);
        mBottomViewPager.addOnPageChangeListener(mPageListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSlidingLayout.removePanelSlideListener(mPanelListener);
        mBottomViewPager.removeOnPageChangeListener(mPageListener);
    }

    public void collapseSlidingPanel() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public boolean isSlidingPanelOpen() {
        return PanelViewModel.isOpenState(mSlidingLayout.getPanelState());
    }

    public boolean isMessagesContainerSelected() {
        return BottomPanelPagerAdapter.isMessagesPage(mBottomViewPager.getCurrentItem());
    }

    private class SlidingPanelListener implements SlidingUpPanelLayout.PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            updateBottomPanelDimensions(slideOffset);
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                        SlidingUpPanelLayout.PanelState newState) {
            sLogger.userInteraction("MainActivity's bottom panel mode changed from "
                    + previousState + " to " + newState);
            mViewModel.onChangePanelState(newState);
        }


        private void updateBottomPanelDimensions(float slideOffset) {
            int height = calculateHeight(slideOffset);
            adjustHeight(height);
        }

        private int calculateHeight(final float slideOffset) {
            int layoutHeight = mSlidingLayout.getHeight();
            int panelHeight = mSlidingLayout.getPanelHeight();

            return (int) ((layoutHeight - panelHeight) * slideOffset);
        }

        private void adjustHeight(int newHeightPxl) {
            final ViewGroup.LayoutParams currentLayoutParams = mBottomViewPager.getLayoutParams();
            currentLayoutParams.height = newHeightPxl;
            mBottomViewPager.setLayoutParams(currentLayoutParams);
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mViewModel.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

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

    @BindView(R.id.main_activity_main_content)
    View mMainActivityContentLayout;

    @BindView(R.id.main_toolbar)
    View mToolbar;

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

    public void setAdapter(BottomPanelPagerAdapter pageAdapter) {
        mBottomViewPager.setAdapter(pageAdapter);
    }

    public void collapseSlidingPanel() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public void anchorSlidingPanel() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    public void changePanelPage(int pageIndex) {
        mBottomViewPager.setCurrentItem(pageIndex);
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

            if (newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
                onPanelAnchored();
            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                onPanelCollapsed();
            }
        }

        private void updateBottomPanelDimensions(float bottomPanelSlideOffset) {
            adjustBottomPanelContentHeight(getScreenWithoutCollapsedPanelHeight(),
                    bottomPanelSlideOffset);
        }

        private int getScreenWithoutCollapsedPanelHeight() {
            int layoutHeight = mSlidingLayout.getHeight();
            int panelHeight = mSlidingLayout.getPanelHeight();
            return layoutHeight - panelHeight;
        }

        private void adjustBottomPanelContentHeight(int screenWithoutCollapsedPanelHeight,
                                                    float slideOffset) {
            int newHeight = (int) (screenWithoutCollapsedPanelHeight * slideOffset);
            int minimumHeight = getBottomPanelMinimumHeight(screenWithoutCollapsedPanelHeight);
            int newHeightPxl = Math.max(newHeight, minimumHeight);
            adjustViewHeight(newHeightPxl, MainActivityPanel.this.mBottomViewPager);
        }

        private int getBottomPanelMinimumHeight(int screenWithoutCollapsedPanelHeight) {
            return (int) (screenWithoutCollapsedPanelHeight * mSlidingLayout.getAnchorPoint());
        }

        private void adjustViewHeight(int newHeightPxl, View view) {
            ViewGroup.LayoutParams currentLayoutParams = view.getLayoutParams();
            currentLayoutParams.height = newHeightPxl;
            view.setLayoutParams(currentLayoutParams);
        }

        private void onPanelCollapsed() {
            adjustMainContentHeight(getScreenWithoutCollapsedPanelHeight(), 0);
        }

        private void onPanelAnchored() {
            adjustMainContentHeight(getScreenWithoutCollapsedPanelHeight(),
                    mSlidingLayout.getAnchorPoint());
        }

        private void adjustMainContentHeight(int screenWithoutCollapsedPanelHeight,
                                             float bottomPanelSlideOffset) {
            int height = (int) (screenWithoutCollapsedPanelHeight *
                    (1 - bottomPanelSlideOffset)) - mToolbar.getHeight();
            adjustViewHeight(height, mMainActivityContentLayout);
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

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

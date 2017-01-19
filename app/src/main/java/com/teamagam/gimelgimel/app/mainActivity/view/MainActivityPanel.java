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

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityPanel extends ActivitySubcomponent implements ViewPager.OnPageChangeListener {

    private static final AppLogger sLogger = AppLoggerFactory.create();

    @Inject
    PanelViewModel mViewModel;

    @BindView(R.id.bottom_swiping_panel)
    ViewPager mBottomViewPager;

    @BindView(R.id.bottom_panel_tabs)
    PagerSlidingTabStrip mTabsStrip;

    @BindView(R.id.activity_main_layout)
    SlidingUpPanelLayout mSlidingLayout;

    @BindArray(R.array.bottom_panel_titles)
    String[] mStringTitles;

    private FragmentManager mFragmentManager;
    private SlidingPanelListener mPanelListener;
    private BottomPanelPagerAdapter mPageAdapter;

    MainActivityPanel(FragmentManager fm, Activity activity) {
        ButterKnife.bind(this, activity);
        ((MainActivity) activity).getMainActivityComponent().inject(this);
        mFragmentManager = fm;
    }

    public void init() {
        mPageAdapter = new BottomPanelPagerAdapter(mFragmentManager, mStringTitles);
        mBottomViewPager.setAdapter(mPageAdapter);
        mTabsStrip.setViewPager(mBottomViewPager);
        mPanelListener = new SlidingPanelListener();

        mViewModel.setView(this);
        mViewModel.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBottomViewPager.addOnPageChangeListener(this);
        mSlidingLayout.addPanelSlideListener(mPanelListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBottomViewPager.removeOnPageChangeListener(this);
        mSlidingLayout.removePanelSlideListener(mPanelListener);
    }

    public void updateUnreadCount(int unreadMessagesCount) {
        mPageAdapter.updateUnreadCount(unreadMessagesCount);
        mPageAdapter.notifyDataSetChanged();
    }

    public void collapseSlidingPanel() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public boolean isSlidingPanelOpen() {
        return isOpenState(mSlidingLayout.getPanelState());
    }

    public boolean isMessagesContainerSelected() {
        return isMessagesPage(mBottomViewPager.getCurrentItem());
    }

    public boolean isMessagesPage(int position) {
        return position == BottomPanelPagerAdapter.MESSAGES_CONTAINER_POSITION;
    }


    public boolean isSensorsPage(int position) {
        return position == BottomPanelPagerAdapter.SENSORS_CONTAINER_POSITION;
    }

    public boolean isClosedState(SlidingUpPanelLayout.PanelState state) {
        return state == SlidingUpPanelLayout.PanelState.COLLAPSED
                || state == SlidingUpPanelLayout.PanelState.HIDDEN;
    }

    public boolean isOpenState(SlidingUpPanelLayout.PanelState state) {
        return state == SlidingUpPanelLayout.PanelState.ANCHORED
                || state == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

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
}

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

public class MainActivityPanel extends ActivitySubcomponent {

    private static final AppLogger sLogger = AppLoggerFactory.create();

    @Inject
    PanelViewModel mViewModel;

    @BindView(R.id.bottom_swiping_panel)
    ViewPager mBottomViewPager;

    @BindView(R.id.bottom_panel_tabs)
    PagerSlidingTabStrip tabsStrip;

    @BindView(R.id.activity_main_layout)
    SlidingUpPanelLayout mSlidingLayout;

    @BindArray(R.array.bottom_panel_titles)
    String[] mStringTitles;

    private FragmentManager mFragmentManager;
    private SlidingPanelListener mPanelListener;


    MainActivityPanel(FragmentManager fm, Activity activity) {
        ButterKnife.bind(this, activity);
        ((MainActivity) activity).getMainActivityComponent().inject(this);
        mFragmentManager = fm;
    }

    public void init() {
        BottomPanelPagerAdapter pageAdapter =
                new BottomPanelPagerAdapter(mFragmentManager, mStringTitles);
        mBottomViewPager.setAdapter(pageAdapter);

        tabsStrip.setViewPager(mBottomViewPager);

        mPanelListener = new SlidingPanelListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSlidingLayout.addPanelSlideListener(mPanelListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSlidingLayout.removePanelSlideListener(mPanelListener);
    }

    public void collapseSlidingPanel() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    public boolean isSlidingPanelOpen() {
        return mSlidingLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED;
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

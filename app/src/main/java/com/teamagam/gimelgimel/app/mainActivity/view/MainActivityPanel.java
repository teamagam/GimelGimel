package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BottomPanelPagerAdapter;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.PanelViewModel;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityPanel {

    @Inject
    PanelViewModel mViewModel;

    @BindView(R.id.bottom_swiping_panel)
    ViewPager mBottomViewPager;

    @BindView(R.id.bottom_panel_tabs)
    PagerSlidingTabStrip tabsStrip;

    @BindArray(R.array.bottom_panel_titles)
    String[] mStringTitles;

    private FragmentManager mFragmentManager;

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
    }

    void adjustHeight(int newHeightPxl) {
        final ViewGroup.LayoutParams currentLayoutParams = mBottomViewPager.getLayoutParams();
        currentLayoutParams.height = newHeightPxl;
        mBottomViewPager.setLayoutParams(currentLayoutParams);
    }
}

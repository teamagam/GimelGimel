package com.teamagam.gimelgimel.app.mainActivity.view;

import android.app.Activity;
import android.databinding.repacked.stringtemplate.v4.STRawGroupDir;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BottomPanelPagerAdapter;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.PanelViewModel;

import java.util.ArrayList;
import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 13/12/2016.
 */

public class MainActivityPanel implements PanelViewModel.PanelDisplayer {

    @Inject
    PanelViewModel mViewModel;

    @BindView(R.id.buttom_swiping_panel)
    ViewPager mButtomViewPager;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    @BindArray(R.array.bottom_panel_titles)
    String[] mStringTitles;

    private FragmentManager mFragmentManager;
    private Activity mActivity;


    MainActivityPanel(FragmentManager fm, Activity activity) {
        mActivity = activity;
        ButterKnife.bind(this, activity);
        ((MainActivity) activity).getMainActivityComponent().inject(this);
        mFragmentManager = fm;

    }

    public void Init() {

        mViewModel.setArguments(mStringTitles, mStringTitles.length);

        BottomPanelPagerAdapter mPageAdapter =
                new BottomPanelPagerAdapter(
                        mFragmentManager,mStringTitles,mStringTitles.length);
        mButtomViewPager.setAdapter(mPageAdapter);

         //Attach the view pager to the tab strip
        tabsStrip.setViewPager(mButtomViewPager);

    }

}

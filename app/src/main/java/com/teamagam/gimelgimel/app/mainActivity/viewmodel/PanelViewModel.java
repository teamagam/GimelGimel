package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Admin on 13/12/2016.
 */

@PerActivity
public class PanelViewModel {

    private String[] mTitles;
    private int mNumOfTitles;


    @Inject
    public PanelViewModel() {}

    public void setArguments (String[] titles, int numOfTitles) {
        mTitles = titles;
        mNumOfTitles = numOfTitles;
    }

    public String[] getTitles() {
        return mTitles;
    }

    public interface PanelDisplayer {

    }
}

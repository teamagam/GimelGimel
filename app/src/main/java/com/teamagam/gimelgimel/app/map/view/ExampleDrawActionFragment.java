package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.R;

public class ExampleDrawActionFragment extends BaseDrawActionFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_example_draw_action;
    }

    @Override
    public void onPositiveButtonClick() {
    }

    @Override
    public String getPositiveButtonText() {
        return getResources().getString(R.string.draw_action_positive_button);
    }
}

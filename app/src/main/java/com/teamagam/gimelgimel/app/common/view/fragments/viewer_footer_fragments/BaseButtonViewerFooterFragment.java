package com.teamagam.gimelgimel.app.common.view.fragments.viewer_footer_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simplifies work with viewer button footers
 * Handles registration self as button listeners
 */
public abstract class BaseButtonViewerFooterFragment extends BaseViewerFooterFragment
        implements View.OnClickListener {

    protected abstract Collection<Integer> getButtonsIds();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        List<Button> buttons = new ArrayList<>();
        for (int buttonId :
                getButtonsIds()) {

            View viewById = view.findViewById(buttonId);
            if (viewById == null) {
                throw new RuntimeException("No button found in view with id " + buttonId);
            }
            if (viewById instanceof Button) {
                Button b = (Button) viewById;
                buttons.add(b);
            }
        }

        setOnClickListeners(this, buttons);

        return view;
    }

    private void setOnClickListeners(View.OnClickListener listener, Collection<Button> buttons) {
        for (Button b : buttons) {
            b.setOnClickListener(listener);
        }
    }
}

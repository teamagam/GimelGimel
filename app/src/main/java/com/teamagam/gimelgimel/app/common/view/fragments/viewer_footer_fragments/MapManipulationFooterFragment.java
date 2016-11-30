package com.teamagam.gimelgimel.app.common.view.fragments.viewer_footer_fragments;

import android.app.DialogFragment;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.common.view.fragments.dialogs.GoToDialogFragment;

import java.util.Arrays;
import java.util.Collection;

/**
 * Displays different map manipulation functionality
 */
public class MapManipulationFooterFragment extends BaseButtonViewerFooterFragment {

    @Override
    protected Collection<Integer> getButtonsIds() {
        return Arrays.asList(
                R.id.map_manipulation_display_center,
                R.id.map_manipulation_go_to,
                R.id.map_manipulation_touched_location
        );
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manipulation_map;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_manipulation_display_center:
                displayCenterCoordinates();
                break;
            case R.id.map_manipulation_go_to:
                openGoToDialog();
                break;
            case R.id.map_manipulation_touched_location:
                Toast.makeText(getActivity(), "not used button", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new RuntimeException("Unknown supported button was clicked");
        }
    }

    private void openGoToDialog() {
        DialogFragment newFragment = new GoToDialogFragment();
        newFragment.show(getActivity().getFragmentManager(), "dialog");
    }

    private void displayCenterCoordinates() {
        getInterface().getGGMap().readAsyncCenterPosition(new ValueCallback<PointGeometryApp>() {
            @Override
            public void onReceiveValue(PointGeometryApp point) {
                Toast.makeText(getActivity(),
                        String.format("N:%.4f E:%.4f", point.latitude,
                                point.longitude), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

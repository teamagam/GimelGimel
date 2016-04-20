package com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments;

import android.app.DialogFragment;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.GoToDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

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
                displayLastTouchedLocation();
                break;
            default:
                throw new RuntimeException("Unknown supported button was clicked");
        }
    }

    private void displayLastTouchedLocation() {
        PointGeometry pg = getInterface().getGGMap().getLastTouchedLocation();
        String toastText;
        if (pg == null) {
            toastText = "No location selected";
        } else {
            toastText = String.format("Lat/Long: %.2f/%.2f",
                    pg.latitude, pg.longitude);
        }
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
    }

    private void openGoToDialog() {
        DialogFragment newFragment = new GoToDialogFragment();
        newFragment.show(getActivity().getFragmentManager(), "dialog");
    }

    private void displayCenterCoordinates() {
        getInterface().getGGMap().readAsyncCenterPosition(new ValueCallback<PointGeometry>() {
            @Override
            public void onReceiveValue(PointGeometry point) {
                Toast.makeText(getActivity(),
                        String.format("N:%.4f E:%.4f", point.latitude,
                                point.longitude), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

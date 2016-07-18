package com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.view.fragments.BaseFragment;
import com.teamagam.gimelgimel.app.view.viewer.GGMap;

/**
 * Holds common logic for over-map button panels overlay with map functionality integration
 * through {@link MapManipulationInterface}
 */
public abstract class BaseViewerFooterFragment extends BaseFragment<GGApplication> {

    private MapManipulationInterface mInterface;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapManipulationInterface) {
            mInterface = (MapManipulationInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapManipulationInterface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity parentActivity = getActivity();
        Fragment targetFragment = getTargetFragment();

        if (parentActivity instanceof MapManipulationInterface) {
            mInterface = (MapManipulationInterface) parentActivity;
        } else if (targetFragment != null &&
                targetFragment instanceof MapManipulationInterface) {
            mInterface = (MapManipulationInterface) targetFragment;
        } else {
            throw new RuntimeException(this.getClass().toString()
                    + " activity/target-fragment must implement fragment's communication interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    protected MapManipulationInterface getInterface() {
        return mInterface;
    }


    /**
     * Interface that must be implemented by the parent activity or
     * the target-fragment of this fragment.
     * <p/>
     * Declares functionality needed by this fragment.
     */
    public interface MapManipulationInterface {

        GGMap getGGMap();
    }
}

package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LogWrapper;
import com.teamagam.gimelgimel.app.common.logging.LogWrapperFactory;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public abstract class BaseFragment<T extends Application> extends Fragment {

    protected T mApp;

    protected LogWrapper sLogger = LogWrapperFactory.create(((Object) this).getClass());

    public BaseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        sLogger.onAttach();
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        sLogger.onCreate();
        super.onCreate(savedInstanceState);

        mApp = (T) (getActivity().getApplicationContext());
    }

    @Override
    @NotNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sLogger.onCreateView();
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        sLogger.onActivityCreated();
        super.onActivityCreated(savedInstanceState);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(doesHaveOptionsMenu());
    }

    @Override
    public void onStart() {
        sLogger.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        sLogger.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        sLogger.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        sLogger.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        sLogger.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        sLogger.onDestroyView();
        super.onDestroy();
    }


    @Override
    public void onDetach() {
        sLogger.onDetach();
        super.onDetach();
    }

    /***
     * Used to represent the title of the screen
     * Override it in your fragment to suggest some title.
     *
     * @return The res title to display
     */
    public int getTitle() {
        return R.string.app_name;
    }

    protected abstract int getFragmentLayout();

    protected boolean doesHaveOptionsMenu() {
        return false;
    }
}

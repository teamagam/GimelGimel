package com.teamagam.gimelgimel.app.view.fragments;


import android.app.FragmentManager;
import android.os.Bundle;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;

/**
 * A {@link BaseFragment} subclass for containing master-detail list messages.
 */
public class MessagesContainerFragment extends BaseFragment<GGApplication> {

    private static final String TAG_FRAGMENT_MESSAGES_MASTER = "TAG_MESSAGES_MASTER_FRAGMENT";
    private MessagesMasterFragment mMessagesMasterFragment;

    public MessagesContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handling dynamic fragments section.
        // If this is the first time the Activity is created (and it's not a restart of it)
        // Else, it's a restart, just fetch the already existing fragments
        if (savedInstanceState == null) {
            mMessagesMasterFragment = new MessagesMasterFragment();
        } else {
            FragmentManager fragmentManager = getFragmentManager();

            mMessagesMasterFragment = (MessagesMasterFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_MESSAGES_MASTER);
        }

        // Don't add the fragment again, if it's already added
        if (!mMessagesMasterFragment.isAdded()) {
            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_messages_master, mMessagesMasterFragment, TAG_FRAGMENT_MESSAGES_MASTER)
                    .commit();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_container;
    }

}

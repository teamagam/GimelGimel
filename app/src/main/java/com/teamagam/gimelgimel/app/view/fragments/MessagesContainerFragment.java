package com.teamagam.gimelgimel.app.view.fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.DisplayMessage;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessagesViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link BaseFragment} subclass for containing master-detail list messages.
 */
public class MessagesContainerFragment extends BaseDataFragment<GGApplication> {

    private MessagesDetailTextFragment mMessagesDetailTextFragment;
    private MessagesDetailGeoFragment mMessagesDetailGeoFragment;

    private MessagesViewModel mMessagesContainerViewModel;


    public MessagesContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NotNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mMessagesContainerViewModel = mApp.getMessagesViewModel();
        mMessagesContainerViewModel.addObserver(this);
        showDetailFragment();
        return rootView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_container;
    }

    @Override
    public void onDataChange() {
        showDetailFragment();
    }

    private void showDetailFragment() {
        DisplayMessage msg = getSelectedMessage();
        if (msg != null) {
            String type = msg.getMessage().getType();
            switch (type) {
                case Message.TEXT:
                    showDetailTextFragment();
                    break;
                case Message.LAT_LONG:
                    showDetailGeoFragment();
                    break;
            }
        }
    }

    private void showDetailGeoFragment() {
        if (mMessagesDetailGeoFragment == null) {
            mMessagesDetailGeoFragment = new MessagesDetailGeoFragment();
        }
        replaceDetailFragment(mMessagesDetailGeoFragment);
    }

    private void showDetailTextFragment() {
        if (mMessagesDetailTextFragment == null) {
            mMessagesDetailTextFragment = new MessagesDetailTextFragment();
        }
        replaceDetailFragment(mMessagesDetailTextFragment);
    }

    private void replaceDetailFragment(MessagesDetailFragment fragmentToAdd) {
        if (!fragmentToAdd.isAdded()) {
            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.message_detail_container, fragmentToAdd)
                    .commit();
        }
    }

    private DisplayMessage getSelectedMessage() {
        MessagesViewModel.DisplayedMessagesRandomAccessor accessor = mMessagesContainerViewModel.getDisplayedMessagesRandomAccessor();
        for (int i = 0; i < accessor.size(); i++) {
            if (accessor.get(i).isSelected())
                return accessor.get(i);
        }
        return null;
    }
}

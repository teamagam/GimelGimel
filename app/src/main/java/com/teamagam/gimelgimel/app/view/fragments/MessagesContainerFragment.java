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

    private static final String TAG_FRAGMENT_MESSAGES_MASTER = "TAG_MESSAGES_MASTER_FRAGMENT";
//    private static final String TAG_FRAGMENT_MESSAGES_DETAIL = "TAG_MESSAGES_DETAIL_FRAGMENT";
private static final String TAG_FRAGMENT_MESSAGES_TEXT_DETAIL =
        "TAG_MESSAGES_DETAIL_TEXT_FRAGMENT";

    private MessagesMasterFragment mMessagesMasterFragment;
    private MessagesDetailFragment mMessagesDetailFragment;
    private MessagesDetailTextFragment mMessagesDetailTextFragment;

    private MessagesViewModel mMessagesContainerViewModel;


    public MessagesContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handling dynamic fragments section.
        // If this is the first time the Fragment is created (and it's not a restart of it)
        // Else, it's a restart, just fetch the already existing fragments
        if (savedInstanceState == null) {
            mMessagesMasterFragment = new MessagesMasterFragment();
        } else {
            FragmentManager fragmentManager = getFragmentManager();

            mMessagesMasterFragment = (MessagesMasterFragment) fragmentManager.findFragmentByTag(
                    TAG_FRAGMENT_MESSAGES_MASTER);
//            mMessagesDetailFragment = (MessagesDetailFragment) fragmentManager.findFragmentByTag(
//                    TAG_FRAGMENT_MESSAGES_DETAIL);
        }

        // Don't add the fragment again, if it's already added
        if (!mMessagesMasterFragment.isAdded()) {
            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_messages_master, mMessagesMasterFragment, TAG_FRAGMENT_MESSAGES_MASTER)
                    .commit();
        }
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
    public void onDataChanged() {
        showDetailFragment();
    }

    private void showDetailFragment() {
        DisplayMessage msg = getSelectedMessage();
        if(msg != null){
            String type = msg.getMessage().getType();
            switch (type){
                case Message.TEXT:
                    showDetailTextFragment();
                    break;
            }
        }
    }

    private void showDetailTextFragment() {
        if(mMessagesDetailTextFragment == null){
            mMessagesDetailTextFragment = new MessagesDetailTextFragment();
        }
        replaceDetailFragment(mMessagesDetailTextFragment);
    }

    private void replaceDetailFragment(MessagesDetailFragment fragmentToAdd) {
        if (!fragmentToAdd.isAdded()) {
            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.message_detail_container, fragmentToAdd,
                    TAG_FRAGMENT_MESSAGES_TEXT_DETAIL)
                    .commit();
        }
//        mMessagesDetailFragment =  (MessagesDetailFragment) getFragmentManager().findFragmentByTag(
//                TAG_FRAGMENT_MESSAGES_DETAIL);
    }

    private DisplayMessage getSelectedMessage() {
        MessagesViewModel.DisplayedMessagesRandomAccessor accessor = mMessagesContainerViewModel.getDisplayedMessagesRandomAccessor();
        for(int i=0; i< accessor.size(); i++){
            if(accessor.get(i).isSelected())
                return accessor.get(i);
        }
        return null;
    }
}

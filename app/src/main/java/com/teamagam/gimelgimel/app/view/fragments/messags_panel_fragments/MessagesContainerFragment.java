package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.ContainerMessagesViewModel;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * A {@link BaseDataFragment} subclass for containing master-detail list messages.
 */
public class MessagesContainerFragment extends BaseDataFragment<GGApplication> {

    @BindView(R.id.fragment_messages_container_title)
    TextView mContainerTitleTV;

    @BindView(R.id.fragment_messages_container_num_unread)
    TextView mNumUnreadTV;

    private MessagesDetailTextFragment mMessagesDetailTextFragment;
    private MessagesDetailGeoFragment mMessagesDetailGeoFragment;
    private MessagesDetailImageFragment mMessagesDetailImageFragment;

    private ContainerMessagesViewModel mMessagesContainerViewModel;

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
        mNumUnreadTV = (TextView) rootView.findViewById(R.id
                .fragment_messages_container_num_unread);
        mMessagesContainerViewModel = mApp.getContainerMessagesViewModel();
        mMessagesContainerViewModel.addObserver(this);
        updateViewsOnUiThread();
        return rootView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_container;
    }

    @Override
    public void updateViewsOnUiThread() {
        updateContainerTitle();
        showDetailFragment();
    }

    private void updateContainerTitle() {
        if (mMessagesContainerViewModel.isAnyMessageSelected()) {
            String title = mMessagesContainerViewModel.getSenderId();
            mContainerTitleTV.setText(title);
        }
        mNumUnreadTV.setText(String.valueOf(mMessagesContainerViewModel.getUnreadMessageCount()));
    }

    private void showDetailFragment() {
        if (mMessagesContainerViewModel.isAnyMessageSelected()) {
            String type = mMessagesContainerViewModel.getType();
            switch (type) {
                case Message.TEXT:
                    showDetailTextFragment();
                    break;
                case Message.LAT_LONG:
                    showDetailGeoFragment();
                    break;
                case Message.IMAGE:
                    showDetailImageFragment();
                    break;
                default:
            }
        }
    }

    private void showDetailImageFragment() {
        if (mMessagesDetailImageFragment == null) {
            mMessagesDetailImageFragment = new MessagesDetailImageFragment();
        }
        replaceDetailFragment(mMessagesDetailImageFragment);
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
}

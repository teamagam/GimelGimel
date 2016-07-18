package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;


import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.ContainerMessagesViewModel;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;

import butterknife.BindView;

/**
 * A {@link BaseDataFragment} subclass for containing master-detail list messages.
 */
public class MessagesContainerFragment extends BaseDataFragment<ContainerMessagesViewModel,
        GGApplication> {

    @BindView(R.id.fragment_messages_container_title)
    TextView mContainerTitleTV;

    @BindView(R.id.fragment_messages_container_num_unread)
    TextView mNumUnreadTV;

    @BindView(R.id.master_detail_layout)
    LinearLayout mMasterDetailLayout;

    private MessagesDetailTextFragment mMessagesDetailTextFragment;
    private MessagesDetailGeoFragment mMessagesDetailGeoFragment;
    private MessagesDetailImageFragment mMessagesDetailImageFragment;

    public MessagesContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getSpecificViewModel() {
        mViewModel = mApp.getContainerMessagesViewModel();
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

    public void onHeightChanged(int height) {
        final ViewGroup.LayoutParams currentLayoutParams = mMasterDetailLayout.getLayoutParams();
        currentLayoutParams.height = height;
        mMasterDetailLayout.setLayoutParams(currentLayoutParams);
    }

    private void updateContainerTitle() {
        if (mViewModel.isAnyMessageSelected()) {
            String title = mViewModel.getSenderId();
            mContainerTitleTV.setText(title);
        }
        mNumUnreadTV.setText(String.valueOf(mViewModel.getUnreadMessageCount()));
    }

    private void showDetailFragment() {
        if (mViewModel.isAnyMessageSelected()) {
            String type = mViewModel.getType();
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

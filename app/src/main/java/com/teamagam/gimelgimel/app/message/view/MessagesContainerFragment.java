package com.teamagam.gimelgimel.app.message.view;


import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.ContainerMessagesViewModel;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments.MessagesDetailFragment;
import com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments.MessagesDetailGeoFragment;
import com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments.MessagesDetailImageFragment;
import com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments.MessagesDetailTextFragment;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;

/**
 * A {@link BaseDataFragment} subclass for containing master-detail list messages.
 */
public class MessagesContainerFragment extends BaseDataFragment<ContainerMessagesViewModel> {

    @BindView(R.id.fragment_messages_container_title)
    TextView mContainerTitleTV;

    @BindView(R.id.fragment_messages_container_num_unread)
    TextView mNumUnreadTV;

    @BindView(R.id.master_detail_layout)
    LinearLayout mMasterDetailLayout;

    @BindView(R.id.fragment_messages_container_nomessage_textview)
    TextView mNoMessageTV;

    @BindString(R.string.message_list_item_time)
    String mTimeFormat;

    //injections
    @Inject
    ContainerMessagesViewModel mViewModel;

    private MessagesDetailTextFragment mMessagesDetailTextFragment;
    private MessagesDetailGeoFragment mMessagesDetailGeoFragment;
    private MessagesDetailImageFragment mMessagesDetailImageFragment;

    public MessagesContainerFragment() {
        // Required empty public constructor
    }

    @Override
    protected ContainerMessagesViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_container;
    }

    public void onHeightChanged(int height) {
        final ViewGroup.LayoutParams currentLayoutParams = mMasterDetailLayout.getLayoutParams();
        currentLayoutParams.height = height;
        mMasterDetailLayout.setLayoutParams(currentLayoutParams);
    }

    private void showDetailFragment() {
        if (mViewModel.isAnyMessageSelected()) {
            String type = mViewModel.getType();
            switch (type) {
                case Message.TEXT:
                    showDetailTextFragment();
                    break;
                case Message.GEO:
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

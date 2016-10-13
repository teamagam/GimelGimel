package com.teamagam.gimelgimel.app.message.view;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.viewModel.ContainerMessagesViewModel;
import com.teamagam.gimelgimel.app.view.MainActivity;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.databinding.FragmentMessagesContainerBinding;
import com.teamagam.gimelgimel.databinding.MessageDetailTitleBinding;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A {@link BaseDataFragment} subclass for containing master-detail list messages.
 */
public class MessagesContainerFragment extends BaseDataFragment<ContainerMessagesViewModel> {

    @BindView(R.id.master_detail_layout)
    LinearLayout mMasterDetailLayout;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentMessagesContainerBinding bind = FragmentMessagesContainerBinding.bind(rootView);
        bind.setViewModel(mViewModel);
        mViewModel.setView(this);
        return bind;
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

    public void showDetailImageFragment() {
        if (mMessagesDetailImageFragment == null) {
            mMessagesDetailImageFragment = new MessagesDetailImageFragment();
        }
        replaceDetailFragment(mMessagesDetailImageFragment);
    }

    public void showDetailGeoFragment() {
        if (mMessagesDetailGeoFragment == null) {
            mMessagesDetailGeoFragment = new MessagesDetailGeoFragment();
        }
        replaceDetailFragment(mMessagesDetailGeoFragment);
    }

    public void showDetailTextFragment() {
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

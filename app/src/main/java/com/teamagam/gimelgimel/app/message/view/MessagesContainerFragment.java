package com.teamagam.gimelgimel.app.message.view;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.viewModel.ContainerMessagesViewModel;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.databinding.FragmentMessagesContainerBinding;

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
        showDetailFragment(new MessagesDetailImageFragment());
    }

    public void showDetailGeoFragment() {
        showDetailFragment(new MessagesDetailGeoFragment());
    }

    public void showDetailTextFragment() {
        showDetailFragment(new MessagesDetailTextFragment());
    }


    private void showDetailFragment(BaseDataFragment mdf) {
        replaceDetailFragment(mdf);
    }

    private void replaceDetailFragment(BaseDataFragment fragmentToAdd) {
        if (!fragmentToAdd.isAdded()) {
            //Set main content viewer fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.message_detail_container, fragmentToAdd)
                    .commit();
        }
    }
}

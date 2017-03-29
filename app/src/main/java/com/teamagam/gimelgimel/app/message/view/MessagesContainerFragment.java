package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.RecyclerFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.message.viewModel.MessagesViewModel;
import com.teamagam.gimelgimel.databinding.FragmentMessagesMasterListBinding;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A fragment representing a list of Messages.
 */
public class MessagesContainerFragment extends RecyclerFragment<MessagesViewModel> {

    @Inject
    MessagesViewModel mViewModel;

    @BindView(R.id.fragment_messages_recycler)
    RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getMainActivity().getMainActivityComponent().inject(this);
        getMainActivity().setOnPanelOpenListener(() -> mViewModel.onPanelOpened());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.addOnScrollListener(new OnLastVisibleItemPositionChangedNotifier());
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getMainActivity().setOnPanelOpenListener(null);
    }

    public void scrollToPosition(int position) {
        mRecyclerView.stopScroll();
        mRecyclerView.getLayoutManager().scrollToPosition(position);
    }

    public boolean isSlidingPanelOpen() {
        return getMainActivity().isSlidingPanelOpen();
    }

    public boolean isBeforeLastMessageVisible() {
        int beforeLastPosition = getLastItemPosition() - 1;
        return getLastVisibleItemPosition() == beforeLastPosition;
    }

    public void displayNewMessageSnackbar(View.OnClickListener onClickListener) {
        Snackbar.make(mRecyclerView, R.string.snackbar_new_message_text, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_new_message_action_text, onClickListener)
                .show();
    }

    public int getLastVisibleItemPosition() {
        return ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                .findLastVisibleItemPosition();
    }

    @Override
    protected MessagesViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentMessagesMasterListBinding bind = FragmentMessagesMasterListBinding.bind(rootView);

        bind.setViewModel(mViewModel);
        mViewModel.setView(this);

        return bind;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_master_list;
    }

    @Override
    protected int getRecyclerId() {
        return R.id.fragment_messages_recycler;
    }

    private MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    private int getLastItemPosition() {
        return mRecyclerView.getAdapter().getItemCount() - 1;
    }

    private class OnLastVisibleItemPositionChangedNotifier extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!isSlidingPanelOpen()) {
                return;
            }
            int lastVisibleItemPosition = getLastVisibleItemPosition();
            if (lastVisibleItemPosition >= 0) {
                mViewModel.onLastVisibleItemPositionChanged(lastVisibleItemPosition);
            }
        }
    }
}

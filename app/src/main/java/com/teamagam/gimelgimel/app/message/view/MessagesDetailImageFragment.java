package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.common.utils.frescoZoomable.CircleProgressBarDrawable;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.message.viewModel.ImageMessageDetailViewModel;
import com.teamagam.gimelgimel.databinding.FragmentMessageImageBinding;

import javax.inject.Inject;

import butterknife.BindView;

public class MessagesDetailImageFragment extends BaseDataFragment<ImageMessageDetailViewModel> {

    @BindView(R.id.image_view)
    SimpleDraweeView mDraweeView;

    @Inject
    ImageMessageDetailViewModel mViewModel;

    public MessagesDetailImageFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_image;
    }

    @Override
    protected void createSpecificViews(View rootView) {
        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
    }

    @Override
    protected ImageMessageDetailViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentMessageImageBinding bind = FragmentMessageImageBinding.bind(rootView);
        bind.setViewModel(mViewModel);
        mViewModel.setView(this);
        return bind;
    }
}


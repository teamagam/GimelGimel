package com.teamagam.gimelgimel.app.message.view;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.viewModel.ImageMessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.drawable.CircleProgressBarDrawable;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A subclass {@link MessagesDetailBaseGeoFragment} for showing Image Messages.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesDetailImageFragment extends MessagesDetailFragment<ImageMessageDetailViewModel> {


    @BindView(R.id.image_view)
    SimpleDraweeView mDraweeView;

    @Inject
    ImageMessageDetailViewModel mViewModel;

    private Uri mUri;

    public MessagesDetailImageFragment() {
        super();
        // Required empty public constructor
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

}


package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.ImageMessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.ImageFullscreenActivity;
import com.teamagam.gimelgimel.app.view.drawable.CircleProgressBarDrawable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yoni on 13 יולי 2016.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesDetailImageFragment extends MessagesDetailFragment {

    @BindView(R.id.image_coord_lat)
    TextView mLatTV;

    @BindView(R.id.image_coord_lon)
    TextView mLonTV;

    @BindView(R.id.image_source_type)
    TextView mSourceTV;

    @BindView(R.id.image_goto_button)
    Button mGoToBtn;

    @BindView(R.id.image_view)
    SimpleDraweeView mDraweeView;



    private ImageMessageDetailViewModel mMessageViewModel;
    private Uri mUri;

    public MessagesDetailImageFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_image_message;
    }

    @Override
    void getSpecificViewModel() {
        mMessageViewModel = mApp.getImageMessageDetailViewModel();
        mMessageViewModel.addObserver(this);

        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
    }

    @OnClick(R.id.image_goto_button)
    public void gotoImageClicked() {
        sLogger.userInteraction("goto button clicked");
        //todo: expand image
    }

    @OnClick(R.id.image_view)
    public void expandViewClicked() {
        sLogger.userInteraction("expand view clicked");

        Intent fullscreenIntent = new Intent(getActivity(), ImageFullscreenActivity.class);
        fullscreenIntent.setData(mUri);
        startActivity(fullscreenIntent);
    }

    @Override
    protected void updateViews() {
        updateTitle(mMessageViewModel.getSenderId(), mMessageViewModel.getDate());
        mSourceTV.setText(mMessageViewModel.getType());

        mUri = Uri.parse(mMessageViewModel.getImageUrl());
        mDraweeView.setImageURI(mUri);

        if (mMessageViewModel.hasLocation() && mMessageViewModel.getPointGeometry() != null) {
            mLatTV.setText(String.valueOf(mMessageViewModel.getPointGeometry().latitude));
            mLonTV.setText(String.valueOf(mMessageViewModel.getPointGeometry().longitude));
        } else {
            mLatTV.setText(R.string.dialog_image_location_unavailable);
            mLonTV.setText(R.string.dialog_image_location_unavailable);
        }

        //todo: add image date
    }
}


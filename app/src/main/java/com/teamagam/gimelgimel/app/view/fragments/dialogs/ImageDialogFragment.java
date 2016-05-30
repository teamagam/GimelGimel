package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.view.drawable.CircleProgressBarDrawable;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Represents a dialog of incoming image messages.
 */
public class ImageDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialog_image_view)
    SimpleDraweeView mDraweeView;
    @BindView(R.id.dialog_image_date)
    TextView mDateTextView;
    @BindView(R.id.dialog_image_coord_lat)
    TextView mLatTextView;
    @BindView(R.id.dialog_image_coord_lon)
    TextView mLonTextView;
    @BindView(R.id.dialog_image_source_type)
    TextView mSourceTextView;
    @BindView(R.id.dialog_image_sender_id)
    TextView mSenderIdTextView;

    private MessageImage mMessage;

    /**
     * Sets the message to show in the dialog.
     * The method will parse the message and update the dialog values
     *
     * @param message - The current message to show
     */
    public void setImageMessage(MessageImage message) {
        mMessage = message;

        // If true, it means the dialog is ready and has the views in it.
        // We can update to view
        if (isAdded())
            updateDialogInfo();
    }

    @Override
    protected int getTitleResId() {
        return R.string.dialog_image_title;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.dialog_image;
    }

    @Override
    protected boolean hasPositiveButton() {
        return true;
    }

    @Override
    protected String getPositiveString() {
        return "Ok";
    }

    @Override
    protected void onCreateDialogLayout(View dialogView) {
        ButterKnife.bind(this, dialogView);

        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());

        updateDialogInfo();
    }

    @Override
    protected Object castInterface(Activity activity) {
        // Currently doesn't have special functionality
        return activity;
    }

    @Override
    protected Object castInterface(Fragment fragment) {
        return fragment;
    }

    /**
     * Updates the dialog view by the data of the message received
     */
    private void updateDialogInfo() {
        if (mMessage != null) {
            ImageMetadata metadata = mMessage.getContent();
            DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
            Uri uri = Uri.parse(metadata.getURL());

            mDraweeView.setImageURI(uri);
            mDateTextView.setText(dateFormat.format(metadata.getTime()));
            mSourceTextView.setText(metadata.getSource());
            mSenderIdTextView.setText(mMessage.getSenderId());

            if (metadata.hasLocation() && metadata.getLocation() != null) {
                String latitude = Double.toString(metadata.getLocation().latitude);
                String longitude = Double.toString(metadata.getLocation().longitude);

                mLatTextView.setText(latitude);
                mLonTextView.setText(longitude);
            } else {
                mLatTextView.setText(R.string.dialog_image_location_unavailable);
                mLonTextView.setText(R.string.dialog_image_location_unavailable);
            }
        }
    }
}

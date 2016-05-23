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

/**
 * Represents a dialog of incoming image messages.
 */
public class ImageDialogFragment extends BaseDialogFragment {

    private SimpleDraweeView mDraweeView;
    private TextView mDateTextView;
    private TextView mLatTextView;
    private TextView mLonTextView;

    private MessageImage mMessage;

    /**
     * Sets the message to show in the dialog.
     * The method will parse the message and update the dialog values
     * @param message - The current message to show
     */
    public void setImageMessage(MessageImage message) {
        mMessage = message;

        updateDialogInfo();
    }

    @Override
    protected int getTitleResId() {
        return R.string.activity_launcher_title;
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
        mDraweeView = (SimpleDraweeView) dialogView.findViewById(R.id.dialog_image_view);
        mDateTextView = (TextView) dialogView.findViewById(R.id.dialog_image_date);
        mLatTextView = (TextView) dialogView.findViewById(R.id.dialog_image_coord_lat);
        mLonTextView = (TextView) dialogView.findViewById(R.id.dialog_image_coord_lon);

        mDraweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
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
        ImageMetadata metadata = mMessage.getContent();
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        Uri uri = Uri.parse(metadata.getURL());
        String latitude = Double.toString(metadata.getLocation().latitude);
        String longitude = Double.toString(metadata.getLocation().longitude);

        mDraweeView.setImageURI(uri);
        mDateTextView.setText(dateFormat.format(mMessage.getCreatedAt()));
        mLatTextView.setText(latitude);
        mLonTextView.setText(longitude);
    }
}

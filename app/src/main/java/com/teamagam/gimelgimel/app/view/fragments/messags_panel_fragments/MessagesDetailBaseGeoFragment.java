package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * A subclass {@link MessagesDetailFragment} for managing listener for goto-button.
 */
public abstract class MessagesDetailBaseGeoFragment<VM extends
        MessageDetailViewModel> extends MessagesDetailFragment<VM> {

    protected GeoMessageInterface mGeoMessageListener;

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachCompat(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachCompat(activity);
        }
    }

    private void onAttachCompat(Context context) {
        if (context instanceof GeoMessageInterface) {
            mGeoMessageListener = (GeoMessageInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GeoMessageInterface");
        }
    }

    protected void gotoLocationClicked(PointGeometry point) {
        sLogger.userInteraction("goto button clicked");
        mGeoMessageListener.goToLocation(point);
    }

    protected void showPinOnMapClicked() {
        sLogger.userInteraction("show pin button clicked");
        mViewModel.drawMessageOnMap(mGeoMessageListener);
    }

    /**
     * Containing activity or target fragment must implement this interface!
     * It is essential for this fragment communication
     */
    public interface GeoMessageInterface {

        void goToLocation(PointGeometry pointGeometry);

        void addMessageLocationPin(Message message);
    }
}

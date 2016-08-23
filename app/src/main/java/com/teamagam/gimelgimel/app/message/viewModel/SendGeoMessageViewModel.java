package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import javax.inject.Inject;

/**
 * View Model that handles send geographic messages from user in
 * {@link SendGeoMessageViewModel}.
  * <p>
 * Controls communication between presenter and view.
 */
@PerActivity
public class SendGeoMessageViewModel {

    private GGMessageSender mSender;

    public PointGeometry mPoint;

    @Inject
    SendGeoMessageViewModel(){

    }

    public void clickedOK() {
        if (isInputValid()) {
//            String type = mGeoTypesSpinner.getSelectedItem().toString();
//            Message sentMessage = mSender.sendGeoMessageAsync(mPoint, mText, type);
//
//            mInterface.drawSentPin(sentMessage);
//            dismiss();
        } else {
            //validate that the user has entered description
//            sLogger.userInteraction("Input not valid");
//            mEditText.setError(mGeoTextValidationError);
//            mEditText.requestFocus();
        }
    }

    private boolean isInputValid() {
//        mText = mEditText.getText().toString();
//        return !mText.isEmpty();
        return true;
    }
}

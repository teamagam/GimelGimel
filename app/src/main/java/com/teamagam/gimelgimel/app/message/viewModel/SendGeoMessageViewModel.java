package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Inject;

/**
 * View Model that handles send geographic messages from user in
 * {@link SendGeoMessageViewModel}.
  * <p>
 * Controls communication between presenter and view.
 */
@PerFragment
public class SendGeoMessageViewModel {

    private GGMessageSender mSender;

    @Inject
    SendGeoMessageViewModel(){

    }


    public void clickedOK() {
//        if (isInputValid()) {
//            String type = mGeoTypesSpinner.getSelectedItem().toString();
//            Message sentMessage = mSender.sendGeoMessageAsync(mPoint, mText, type);
//
//            mInterface.drawSentPin(sentMessage);
//            dismiss();
//        } else {
//            //validate that the user has entered description
//            sLogger.userInteraction("Input not valid");
//            mEditText.setError(mGeoTextValidationError);
//            mEditText.requestFocus();
//        }
    }
}

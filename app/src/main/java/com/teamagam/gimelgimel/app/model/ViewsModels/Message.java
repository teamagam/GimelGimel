package com.teamagam.gimelgimel.app.model.ViewsModels;

import java.util.Date;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */

/**
 * A class representing a type of ic_message passed to the server
 */
public class Message {

    String mMessageId;
    String mSenderId;
    Date mCreatedAt;
    String mType;
    MessageContent mContent;

}

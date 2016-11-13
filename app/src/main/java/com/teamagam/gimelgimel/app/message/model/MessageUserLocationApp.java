package com.teamagam.gimelgimel.app.message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;
import com.teamagam.gimelgimel.app.message.model.contents.LocationSample;

/**
 * UserLocation-Type class for {@link MessageApp}'s inner content
 */
public class MessageUserLocationApp extends MessageApp<LocationSample> implements Parcelable{

    public MessageUserLocationApp(LocationSample sample) {
        super(MessageApp.USER_LOCATION);
        mContent = sample;
    }

    protected MessageUserLocationApp(Parcel in) {
        super(in);
        mContent = in.readParcelable(LocationSample.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mContent, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageUserLocationApp> CREATOR = new Creator<MessageUserLocationApp>() {
        @Override
        public MessageUserLocationApp createFromParcel(Parcel in) {
            return new MessageUserLocationApp(in);
        }

        @Override
        public MessageUserLocationApp[] newArray(int size) {
            return new MessageUserLocationApp[size];
        }
    };

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

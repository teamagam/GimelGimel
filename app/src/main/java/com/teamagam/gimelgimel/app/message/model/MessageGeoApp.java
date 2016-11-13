package com.teamagam.gimelgimel.app.message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;

/**
 * A class for geo messages (using GeoContentData as its content)
 */
public class MessageGeoApp extends MessageApp<GeoContentApp>{

    public MessageGeoApp(GeoContentApp content) {
        super(MessageApp.GEO);
        mContent = content;
    }

    protected MessageGeoApp(Parcel in) {
        super(in);
        mContent = in.readParcelable(GeoContentApp.class.getClassLoader());
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

    public static final Creator<MessageGeoApp> CREATOR = new Creator<MessageGeoApp>() {
        @Override
        public MessageGeoApp createFromParcel(Parcel in) {
            return new MessageGeoApp(in);
        }

        @Override
        public MessageGeoApp[] newArray(int size) {
            return new MessageGeoApp[size];
        }
    };

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }


}



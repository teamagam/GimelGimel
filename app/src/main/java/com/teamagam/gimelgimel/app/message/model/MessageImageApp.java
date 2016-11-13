package com.teamagam.gimelgimel.app.message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link MessageApp}'s inner content
 */
public class MessageImageApp extends MessageApp<ImageMetadataApp> {

    public MessageImageApp(ImageMetadataApp meta) {
        super(MessageApp.IMAGE);
        mContent = meta;
    }

    protected MessageImageApp(Parcel in) {
        super(in);
        mContent = in.readParcelable(ImageMetadataApp.class.getClassLoader());

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

    public static final Creator<MessageImageApp> CREATOR = new Creator<MessageImageApp>() {
        @Override
        public MessageImageApp createFromParcel(Parcel in) {
            return new MessageImageApp(in);
        }

        @Override
        public MessageImageApp[] newArray(int size) {
            return new MessageImageApp[size];
        }
    };

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

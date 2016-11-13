package com.teamagam.gimelgimel.app.message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;

/**
 * Text-Type class for {@link MessageApp}'s inner content
 */
public class MessageTextApp extends MessageApp<String> implements Parcelable{

    public MessageTextApp(String text) {
        super(MessageApp.TEXT);
        mContent = text;
    }

    protected MessageTextApp(Parcel in) {
        super(in);
        mContent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mContent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageTextApp> CREATOR = new Creator<MessageTextApp>() {
        @Override
        public MessageTextApp createFromParcel(Parcel in) {
            return new MessageTextApp(in);
        }

        @Override
        public MessageTextApp[] newArray(int size) {
            return new MessageTextApp[size];
        }
    };

    @Override
    public void accept(IMessageAppVisitor visitor) {
        visitor.visit(this);
    }
}

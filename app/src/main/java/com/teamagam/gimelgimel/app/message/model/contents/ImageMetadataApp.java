package com.teamagam.gimelgimel.app.message.model.contents;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * Created on 5/18/2016.
 * MessageImageData Content.
 */
public class ImageMetadataApp implements Parcelable {


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({USER, SENSOR})
    public @interface SourceType {
    }

    public static final String USER = "User";
    public static final String SENSOR = "Sensor";

    private String mEntityId;
    private long mTime;
    private boolean mHasLocation = false;
    /*@SourceType*/
    private String mSource;
    private String mURL;

    /**
     * Construct a new Image Metadata that has time, source, location and URL.
     */
    public ImageMetadataApp(long time, String url, String entityId, /*@SourceType*/ String source) {
        mTime = time;
        mSource = source;
        mURL = url;
        mEntityId = entityId;
        mHasLocation = mEntityId != null;
    }


    protected ImageMetadataApp(Parcel in) {
        mEntityId = in.readString();
        mTime = in.readLong();
        mHasLocation = in.readByte() != 0;
        mSource = in.readString();
        mURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEntityId);
        dest.writeLong(mTime);
        dest.writeByte((byte) (mHasLocation ? 1 : 0));
        dest.writeString(mSource);
        dest.writeString(mURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageMetadataApp> CREATOR = new Creator<ImageMetadataApp>() {
        @Override
        public ImageMetadataApp createFromParcel(Parcel in) {
            return new ImageMetadataApp(in);
        }

        @Override
        public ImageMetadataApp[] newArray(int size) {
            return new ImageMetadataApp[size];
        }
    };

    public String getEntityId() {
        return mEntityId;
    }

    /**
     * Return the UTC time of this fix, in milliseconds since January 1, 1970.
     * <p/>
     *
     * @return time of fix, in milliseconds since January 1, 1970.
     */
    public long getTime() {
        return mTime;
    }

    /**
     * True if this has location.
     */
    public boolean hasLocation() {
        return mHasLocation;
    }

    @SourceType
    public String getSource() {
        return mSource;
    }

    /**
     * returns URL
     *
     * @return url, may be null.
     */
    public String getURL() {
        return mURL;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ImageMetadataData[");
        s.append(mSource);
        s.append(": ");
        if (mTime == 0) {
            s.append(" t=?!?");
        } else {
            s.append(" t=");
            s.append(new Date(mTime));
        }

        if (hasLocation()) {
            s.append(" ");
            s.append(mEntityId);
        }

        if (mURL != null) {
            s.append(" url=");
            s.append(mURL);
        }
        s.append(']');
        return s.toString();
    }
}

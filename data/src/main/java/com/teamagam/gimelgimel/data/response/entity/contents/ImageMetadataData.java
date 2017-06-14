package com.teamagam.gimelgimel.data.response.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.geogson.core.model.Point;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ImageMetadataData {

  public static final String USER = "User";
  public static final String SENSOR = "Sensor";
  @SerializedName("geometry")
  private Point mPoint;
  @SerializedName("timeStamp")
  private long mTime;
  @SourceType
  @SerializedName("sourceType")
  private String mSource;
  @SerializedName("url")
  private String mRemoteUrl;
  @SerializedName("base64")
  private String mBase64;
  private transient String mLocalUrl;

  /**
   * Construct a new Image Metadata that has only time and source.
   * time and source are must
   */
  public ImageMetadataData(long time, @SourceType String source) {
    mTime = time;
    mSource = source;
  }

  /**
   * Construct a new Image Metadata that has only time, source and URL.
   * time and source are must
   */
  public ImageMetadataData(long time,
      String remoteUrl,
      String localUrl,
      @SourceType String source) {
    mTime = time;
    mSource = source;
    mRemoteUrl = remoteUrl;
    mLocalUrl = localUrl;
  }

  /**
   * Construct a new Image Metadata that has time, source and location W/O URL.
   */
  public ImageMetadataData(long time, Point loc, @SourceType String source) {
    mTime = time;
    mSource = source;
    mPoint = loc;
  }

  /**
   * Construct a new Image Metadata that has time, source, location and URL.
   */
  public ImageMetadataData(long time,
      String remoteUrl,
      String localUrl,
      Point loc,
      @SourceType String source) {
    mTime = time;
    mSource = source;
    mPoint = loc;
    mRemoteUrl = remoteUrl;
    mLocalUrl = localUrl;
  }

  /**
   * Return the UTC time of this fix, in milliseconds since January 1, 1970.
   * <p>
   *
   * @return time of fix, in milliseconds since January 1, 1970.
   */
  public long getTime() {
    return mTime;
  }

  /**
   * Get the location..
   * <p>
   *
   * @return Location
   */
  public Point getLocation() {
    if (mPoint != null) {
      return Point.from(mPoint.coordinates());
    } else {
      return null;
    }
  }

  /**
   * Set the location..
   * <p>
   *
   * @return Location
   */
  public void setLocation(Point point) {
    mPoint = point;
  }

  @SourceType
  public String getSource() {
    return mSource;
  }

  public void setURL(String url) {
    mRemoteUrl = url;
  }

  /**
   * returns remote URL
   *
   * @return url, may be null.
   */
  public String getRemoteUrl() {
    return mRemoteUrl;
  }

  public String getLocalUrl() {
    return mLocalUrl;
  }

  public String getBase64() {
    return mBase64;
  }

  public void setBase64(String base64) {
    mBase64 = base64;
  }

  @Retention(RetentionPolicy.SOURCE)
  public @interface SourceType {
  }
}

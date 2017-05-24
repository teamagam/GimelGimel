package com.teamagam.gimelgimel.app.message.model.contents;

import android.support.annotation.StringDef;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * Created on 5/18/2016.
 * MessageImageData Content.
 */
public class ImageMetadataApp {

  public static final String USER = "User";
  public static final String SENSOR = "Sensor";
  private GeoEntity mGeoEntity;
  private long mTime;
  private boolean mHasLocation = false;
  /*@SourceType*/
  private String mSource;
  private String mURL;

  /**
   * Construct a new Image Metadata that has time, source, location and URL.
   */
  public ImageMetadataApp(long time, String url, String source, GeoEntity geoEntity) {
    mTime = time;
    mSource = source;
    mURL = url;
    mGeoEntity = geoEntity;
    mHasLocation = geoEntity != null;
  }

  public GeoEntity getGeoEntity() {
    return mGeoEntity;
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
      s.append(mGeoEntity);
    }

    if (mURL != null) {
      s.append(" url=");
      s.append(mURL);
    }
    s.append(']');
    return s.toString();
  }

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({ USER, SENSOR })
  public @interface SourceType {
  }
}

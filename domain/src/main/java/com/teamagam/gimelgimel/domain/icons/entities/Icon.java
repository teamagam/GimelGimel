package com.teamagam.gimelgimel.domain.icons.entities;

import java.net.URL;

public class Icon {
  private String mId;
  private URL mUrl;
  private String mDisplayName;

  public Icon(String id, URL url, String displayName) {
    mId = id;
    mUrl = url;
    mDisplayName = displayName;
  }

  public String getId() {
    return mId;
  }

  public URL getUrl() {
    return mUrl;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  @Override
  public String toString() {
    return getDisplayName();
  }
}

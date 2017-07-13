package com.teamagam.gimelgimel.domain.icons.entities;

import java.net.URL;

public class ServerIcon {
  private String mId;
  private URL mUrl;
  private String mDisplayNameEng;
  private String mDisplayNameHeb;

  public ServerIcon(String id, URL url, String displayNameEng, String displayNameHeb) {
    mId = id;
    mUrl = url;
    mDisplayNameEng = displayNameEng;
    mDisplayNameHeb = displayNameHeb;
  }

  public String getId() {
    return mId;
  }

  public URL getUrl() {
    return mUrl;
  }

  public String getDisplayNameEng() {
    return mDisplayNameEng;
  }

  public String getDisplayNameHeb() {
    return mDisplayNameHeb;
  }
}

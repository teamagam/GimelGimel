package com.teamagam.gimelgimel.data.response.entity;

import com.google.gson.annotations.SerializedName;
import java.net.URL;

public class ServerIconResponse {
  @SerializedName("_id")
  private String id;
  @SerializedName("url")
  private URL url;
  @SerializedName("displayNameEng")
  private String displayNameEng;
  @SerializedName("displayNameHeb")
  private String displayNameHeb;

  public String getId() {
    return id;
  }

  public URL getUrl() {
    return url;
  }

  public String getDisplayNameEng() {
    return displayNameEng;
  }

  public String getDisplayNameHeb() {
    return displayNameHeb;
  }
}

package com.teamagam.gimelgimel.domain.layers.entitiy;

import java.net.URL;

public class VectorLayer {
  private final String mId;
  private String mLayerName;
  private URL mUrl;
  private Severity mSeverity;
  private Category mCategory;
  private int mVersion;

  public VectorLayer(String id,
      String layerName,
      URL url,
      Severity severity,
      Category category,
      int version) {
    mId = id;
    mLayerName = layerName;
    mUrl = url;
    mSeverity = severity;
    mCategory = category;
    mVersion = version;
  }

  public static VectorLayer copyWithDifferentSeverity(VectorLayer vectorLayer, Severity severity) {
    return new VectorLayer(vectorLayer.getId(), vectorLayer.getName(), vectorLayer.getUrl(),
        severity, vectorLayer.getCategory(), vectorLayer.getVersion());
  }

  private static <T extends Enum<T>> T parseCaseInsensitive(Class<T> c, String string) {
    return Enum.valueOf(c, string.toUpperCase());
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mLayerName;
  }

  public void setLayerName(String layerName) {
    mLayerName = layerName;
  }

  public URL getUrl() {
    return mUrl;
  }

  public void setURl(URL url) {
    mUrl = url;
  }

  public int getVersion() {
    return mVersion;
  }

  public void setVersion(int version) {
    mVersion = version;
  }

  public Severity getSeverity() {
    return mSeverity;
  }

  public void setSeverity(Severity severity) {
    mSeverity = severity;
  }

  public Category getCategory() {
    return mCategory;
  }

  public void setCategory(Category category) {
    mCategory = category;
  }

  public boolean isImportant() {
    return mSeverity == Severity.IMPORTANT;
  }

  @Override
  public String toString() {
    return String.format("NAME=%s \r\n VERSION=%d \r\n", mLayerName, mVersion);
  }

  public enum Severity {
    REGULAR,
    IMPORTANT;

    public static Severity parseCaseInsensitive(String string) {
      return VectorLayer.parseCaseInsensitive(Severity.class, string);
    }
  }

  public enum Category {
    FIRST,
    SECOND;

    public static Category parseCaseInsensitive(String string) {
      return VectorLayer.parseCaseInsensitive(Category.class, string);
    }
  }
}
package com.teamagam.gimelgimel.domain.messages.entity.contents;

public class VectorLayerContent {

  private final String mId;
  private String mName;
  private int mVersion;
  private Severity mSeverity;
  private Category mCategory;

  public VectorLayerContent(String id,
      String name,
      int version,
      Severity severity,
      Category category) {
    mId = id;
    mName = name;
    mVersion = version;
    mSeverity = severity;
    mCategory = category;
  }

  public static VectorLayerContent copyWithDifferentSeverity(VectorLayerContent vectorLayerContent,
      Severity severity) {
    return new VectorLayerContent(vectorLayerContent.getId(), vectorLayerContent.getName(),
        vectorLayerContent.getVersion(), severity, vectorLayerContent.getCategory());
  }

  private static <T extends Enum<T>> T parseCaseInsensitive(Class<T> c, String string) {
    return Enum.valueOf(c, string.toUpperCase());
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public int getVersion() {
    return mVersion;
  }

  public Severity getSeverity() {
    return mSeverity;
  }

  public Category getCategory() {
    return mCategory;
  }

  public boolean isImportant() {
    return mSeverity == Severity.IMPORTANT;
  }

  @Override
  public String toString() {
    return String.format("ID=%s \r\n NAME=%s \r\n VERSION=%d \r\n", mId, mName, mVersion);
  }

  public enum Severity {
    REGULAR,
    IMPORTANT;

    public static Severity parseCaseInsensitive(String string) {
      return VectorLayerContent.parseCaseInsensitive(Severity.class, string);
    }
  }

  public enum Category {
    FIRST,
    SECOND;

    public static Category parseCaseInsensitive(String string) {
      return VectorLayerContent.parseCaseInsensitive(Category.class, string);
    }
  }
}
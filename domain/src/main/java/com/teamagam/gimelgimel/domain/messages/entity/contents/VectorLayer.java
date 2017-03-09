package com.teamagam.gimelgimel.domain.messages.entity.contents;

public class VectorLayer {

    private final String mId;
    private String mName;
    private int mVersion;
    private Severity mSeverity;
    private Category mCategory;

    public static VectorLayer copyWithDifferentSeverity(VectorLayer vectorLayer,
                                                        Severity severity) {
        return new VectorLayer(vectorLayer.getId(),
                vectorLayer.getName(),
                vectorLayer.getVersion(),
                severity,
                vectorLayer.getCategory());
    }

    public VectorLayer(String id, String name, int version, Severity severity, Category category) {
        mId = id;
        mName = name;
        mVersion = version;
        mSeverity = severity;
        mCategory = category;
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
        REGULAR, IMPORTANT;

        public static Severity parseCaseInsensitive(String string) {
            return VectorLayer.parseCaseInsensitive(Severity.class, string);
        }
    }

    public enum Category {
        FIRST, SECOND;

        public static Category parseCaseInsensitive(String string) {
            return VectorLayer.parseCaseInsensitive(Category.class, string);
        }
    }

    private static <T extends Enum<T>> T parseCaseInsensitive(Class<T> c, String string) {
        return Enum.valueOf(c, string.toUpperCase());
    }
}
package httpModels.contents;

import com.google.gson.annotations.SerializedName;

import httpModels.geometries.PointGeometry;

/**
 * A class represents a location pinned by the user
 */
public class GeoContent {

    @SerializedName("location")
    private PointGeometry mPoint;

    @SerializedName("text")
    private String mText;

    @SerializedName("locationType")
    private String mType;


    public GeoContent(PointGeometry point, String text, String type) {
        this.mPoint = point;
        this.mText = text;
        this.mType = type;
    }

    public PointGeometry getPointGeometry() {
        return mPoint;
    }

    public String getText() {
        return mText;
    }

    public String getType() {
        return mType;
    }
}

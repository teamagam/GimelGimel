package com.teamagam.gimelgimel.app.view.viewer.data.symbols;

/**
 * Created by Bar on 03-Mar-16.
 */
public class PointImageSymbol implements PointSymbol {

    private String mImageUrl;
    private int mPixelWidth;
    private int mPixelHeight;

    public PointImageSymbol(String imageUrl, int pixelWidth, int pixelHeight) {
        this.mImageUrl = imageUrl;
        this.mPixelWidth = pixelWidth;
        this.mPixelHeight = pixelHeight;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public int getPixelWidth() {
        return mPixelWidth;
    }

    public int getPixelHeight() {
        return mPixelHeight;
    }


    //Should be inside cesium-related class. not here.
//    @Override
//    public String toJson() {
//        return String.format("{\n" +
//                "      image : '%s',\n" +
//                "      width : %d,\n" +
//                "      height : %d\n" +
//                "  }", this.mImageUrl, this.mPixelWidth, this.mPixelHeight);
//    }
}

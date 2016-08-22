package com.teamagam.gimelgimel.app.map.model.symbols;

/**
 * Created by Bar on 03-Mar-16.
 */
public class PointImageSymbol implements PointSymbol {

    private String mImageUrl;
    private int mPixelWidth;
    private int mPixelHeight;

    public PointImageSymbol(String imageUrl, int pixelWidth, int pixelHeight) {
        mImageUrl = imageUrl;
        mPixelWidth = pixelWidth;
        mPixelHeight = pixelHeight;
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
}

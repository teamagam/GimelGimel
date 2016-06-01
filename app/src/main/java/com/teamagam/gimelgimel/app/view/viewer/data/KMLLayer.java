package com.teamagam.gimelgimel.app.view.viewer.data;

/**
 * Created by Yoni on 3/7/2016.
 */
public class KMLLayer extends GGLayer{

//    private String mId;
    private String mPath;

    public KMLLayer(String id, String path) {
        super(id);
        mPath = path;
    }

    public String getPath(){
        return mPath;
    }

}

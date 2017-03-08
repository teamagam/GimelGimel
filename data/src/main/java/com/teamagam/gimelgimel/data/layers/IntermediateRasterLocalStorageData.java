package com.teamagam.gimelgimel.data.layers;


import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.layers.IntermediateRasterLocalStorage;
import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class IntermediateRasterLocalStorageData implements IntermediateRasterLocalStorage {

    private File mRastersDir;

    public IntermediateRasterLocalStorageData(File baseDir) {
        mRastersDir = new File(baseDir +
                File.separator +
                Constants.INTERMEDIATE_RASTERS_CACHE_DIR_NAME);
    }

    @Override
    public Iterable<IntermediateRaster> getExistingLayers() {
        return transformToRaster(getFiles());
    }

    private Iterable<IntermediateRaster> transformToRaster(File[] files) {
        ArrayList<IntermediateRaster> rasters = new ArrayList<>();

        for (File file : files) {
            String name = file.getName();
            URI uri = file.toURI();
            IntermediateRaster raster = new IntermediateRaster(name, uri);

            rasters.add(raster);
        }

        return rasters;
    }

    private File[] getFiles() {
        return mRastersDir.listFiles();
    }
}

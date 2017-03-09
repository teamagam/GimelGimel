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
                Constants.RASTERS_CACHE_DIR_NAME);
    }

    @Override
    public Iterable<IntermediateRaster> getExistingRasters() {
        return transformToRaster(getFiles());
    }

    private Iterable<IntermediateRaster> transformToRaster(File[] files) {
        ArrayList<IntermediateRaster> rasters = new ArrayList<>();

        for (File file : files) {
            rasters.add(transformToRaster(file));
        }

        return rasters;
    }

    private IntermediateRaster transformToRaster(File file) {
        String name = file.getName();
        URI uri = file.toURI();

        return new IntermediateRaster(name, uri);
    }

    private File[] getFiles() {
        return mRastersDir.listFiles();
    }
}

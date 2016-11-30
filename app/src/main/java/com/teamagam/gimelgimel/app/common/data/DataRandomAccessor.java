package com.teamagam.gimelgimel.app.common.data;

/**
 * TODO: add class summary notes
 */
public interface DataRandomAccessor<DATA> {
    int size();

    DATA get(int index);
}

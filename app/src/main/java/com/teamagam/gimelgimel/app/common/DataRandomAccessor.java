package com.teamagam.gimelgimel.app.common;

/**
 * TODO: add class summary notes
 */
public interface DataRandomAccessor<DATA> {
    int size();

    DATA get(int index);
}

package com.teamagam.gimelgimel.app.common;

import com.teamagam.gimelgimel.app.message.model.MessageApp;

/**
 * TODO: add class summary notes
 */
public interface DataRandomAccessor<DATA> {
    int size();

    DATA get(int index);


}

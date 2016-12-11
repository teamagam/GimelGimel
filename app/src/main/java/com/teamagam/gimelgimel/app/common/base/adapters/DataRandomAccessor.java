package com.teamagam.gimelgimel.app.common.base.adapters;

public interface DataRandomAccessor<DATA extends IdentifiedData> {
    int size();

    DATA get(int index);

    void add(DATA data);

    int getPosition(String dataId);

    void replace(int index, DATA newData);
}

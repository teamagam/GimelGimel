package com.teamagam.gimelgimel.app.common.base.adapters;

public interface DataRandomAccessor<DATA extends IdentifiedData> {
    int size();

    void add(DATA data);

    void remove(String dataId);

    DATA get(int position);

    int getPosition(String dataId);
}

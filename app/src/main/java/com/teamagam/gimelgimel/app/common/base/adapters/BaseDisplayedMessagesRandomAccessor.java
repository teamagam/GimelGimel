package com.teamagam.gimelgimel.app.common.base.adapters;

import java.util.HashMap;
import java.util.Map;

public class BaseDisplayedMessagesRandomAccessor<DATA extends IdentifiedData> implements DataRandomAccessor<DATA> {

    private final Map<Integer, DATA> mDataByPosition;
    private final Map<String, Integer> mPositionById;

    private int mCount;

    public BaseDisplayedMessagesRandomAccessor() {
        mDataByPosition = new HashMap<>();
        mPositionById = new HashMap<>();
        mCount = 0;
    }

    @Override
    public void add(DATA data) {
        if (mPositionById.containsKey(data.getId())) {
            throw new IllegalStateException("Accessor already contains this data element");
        }

        mDataByPosition.put(mCount, data);
        mPositionById.put(data.getId(), mCount);

        mCount++;
    }

    @Override
    public int getPosition(String dataId) {
        Integer integer = mPositionById.get(dataId);
        return integer != null ? integer : -1;
    }

    @Override
    public void replace(int index, DATA newData) {
        mDataByPosition.put(index, newData);
    }

    @Override
    public int size() {
        return mCount;
    }

    @Override
    public DATA get(int index) {
        return mDataByPosition.get(index);
    }
}

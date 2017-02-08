package com.teamagam.gimelgimel.app.common.base.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class BaseDisplayedMessagesRandomAccessor<DATA extends IdentifiedData> implements DataRandomAccessor<DATA> {

    private final SortedSet<DATA> mSortedDataSet;
    private final Map<Integer, DATA> mDataByPosition;
    private final Map<String, DATA> mDataById;

    public BaseDisplayedMessagesRandomAccessor() {
        mSortedDataSet = new TreeSet<>();
        mDataByPosition = new HashMap<>();
        mDataById = new HashMap<>();
    }

    @Override
    public int size() {
        return mSortedDataSet.size();
    }

    @Override
    public void add(DATA data) {
        checkForNonExistence(data);
        put(data);
        int position = getPositionByData(data);
        insertInTheMiddle(position, data);
    }

    @Override
    public void remove(String dataId) {
        int position = removeAndGetLastPosition(dataId);
        adjustPositionsFrom(position);
    }

    @Override
    public DATA get(int position) {
        checkForExistence(position);
        return mDataByPosition.get(position);
    }

    @Override
    public int getPosition(String dataId) {
        if (mDataById.containsKey(dataId)) {
            DATA data = mDataById.get(dataId);
            return getPositionByData(data);
        } else {
            return -1;
        }
    }

    private void checkForNonExistence(DATA data) {
        if (mSortedDataSet.contains(data)) {
            throw new IllegalStateException(
                    String.format("Accessor already contains this data element: %s\nid: %s",
                            data.toString(), data.getId()));
        }
    }

    private void put(DATA data) {
        mSortedDataSet.add(data);
        mDataById.put(data.getId(), data);
    }

    private void insertInTheMiddle(int position, DATA newData) {
        DATA tempData = mDataByPosition.remove(position);
        mDataByPosition.put(position, newData);
        if (tempData != null) {
            insertInTheMiddle(position + 1, tempData);
        }
    }

    private int removeAndGetLastPosition(String dataId) {
        DATA data = mDataById.remove(dataId);
        int position = getPositionByData(data);
        mSortedDataSet.remove(data);
        return position;
    }

    private void adjustPositionsFrom(int position) {
        int nextPos = position + 1;
        DATA data = mDataByPosition.remove(nextPos);
        if (data != null) {
            mDataByPosition.put(position, data);
            adjustPositionsFrom(nextPos);
        }
    }

    private void checkForExistence(int position) {
        if (!mDataByPosition.containsKey(position)) {
            throw new IllegalStateException(
                    String.format("There is no data element at position %d.", position));
        }
    }

    private int getPositionByData(DATA data) {
        return mSortedDataSet.headSet(data).size();
    }

}

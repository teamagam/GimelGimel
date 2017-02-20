package com.teamagam.gimelgimel.app.common.utils;

import java.util.Map;
import java.util.TreeMap;

public class BiMap<TKey, TValue> {

    private final Map<TKey, TValue> mMap;
    private final Map<TValue, TKey> mInverseMap;

    public BiMap() {
        mMap = new TreeMap<>();
        mInverseMap = new TreeMap<>();
    }

    public TValue getValue(TKey key) {
        return mMap.get(key);
    }

    public TKey getKey(TValue value) {
        return mInverseMap.get(value);
    }

    public boolean containsKey(TKey key) {
        return mMap.containsKey(key);
    }

    public boolean containsValue(TValue value) {
        return mInverseMap.containsKey(value);
    }

    public void removeByKey(TKey key) {
        TValue value = mMap.get(key);
        mMap.remove(key);
        mInverseMap.remove(value);
    }

    public void removeByValue(TValue value) {
        TKey key = mInverseMap.get(value);
        mInverseMap.remove(value);
        mMap.remove(key);
    }

    public int size() {
        return mMap.size();
    }

    public void put(TKey key, TValue value) {
        cleanPreviousMappings(key, value);
        setNewMappings(key, value);
    }

    private void cleanPreviousMappings(TKey key, TValue value) {
        mMap.remove(key);
        mInverseMap.remove(value);
    }

    private void setNewMappings(TKey key, TValue value) {
        mMap.put(key, value);
        mInverseMap.put(value, key);
    }
}

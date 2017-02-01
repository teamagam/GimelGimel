package com.teamagam.gimelgimel.app.common.utils;

import java.util.HashMap;

public class StringToIntegerMapper {

    private HashMap<Integer, String> mIntToStringMap;

    public StringToIntegerMapper() {
        mIntToStringMap = new HashMap<>();
    }

    public boolean contains(String stringId) {
        return mIntToStringMap.containsKey(stringId.hashCode());
    }

    public int get(String stringId) {
        int key = stringId.hashCode();
        if (!mIntToStringMap.containsKey(key)) {
            throw new RuntimeException("Mapper does not contain ID: " + stringId);
        }
        return key;
    }

    public int put(String stringId) {
        int intId = stringId.hashCode();
        mIntToStringMap.put(intId, stringId);
        return intId;
    }

    public String get(int intId) {
        return mIntToStringMap.get(intId);
    }
}

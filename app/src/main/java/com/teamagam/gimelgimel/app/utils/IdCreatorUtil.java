package com.teamagam.gimelgimel.app.utils;

import java.util.UUID;

/**
 * Created by Gil.Raytan on 30-Mar-16.
 */
public class IdCreatorUtil {

    public static String getId()
    {
        return java.util.UUID.randomUUID().toString();
    }
}

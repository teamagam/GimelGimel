package com.teamagam.gimelgimel.app.common.utils;

import android.content.Context;
import android.content.res.Resources;

import com.teamagam.gimelgimel.R;

import java.util.Random;

public class UsernameGenerator {

    private static final String SPACE = " ";
    private static final Random rand = new Random();

    public static String generateUsername(Context context) {
        Resources res = context.getResources();
        String[] animals = res.getStringArray(R.array.name_generator_animals);
        String[] adjectives = res.getStringArray(R.array.name_generator_adjectives);
        return capitalizeFirstLetters(adjectives[rand.nextInt(adjectives.length)]) + SPACE +
                capitalizeFirstLetters(animals[rand.nextInt(animals.length)]);
    }

    public static String capitalizeFirstLetters(String source) {
        StringBuilder builder = new StringBuilder();

        String[] strArr = source.split(SPACE);
        for (String str : strArr) {
            builder.append(str.substring(0, 1).toUpperCase() + str.substring(1))
                    .append(SPACE);
        }

        return builder.toString().trim();
    }

}
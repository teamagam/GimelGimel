package com.teamagam.gimelgimel.app.map.esri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KmlBalloonDataParser {

    private String mBalloonData;


    public KmlBalloonDataParser(String balloonData) {
        mBalloonData = balloonData;
    }

    public String parseName() {
        return getValueFromBalloon("name");
    }

    public String parseDescription() {
        return getValueFromBalloon("description");
    }

    private String getValueFromBalloon(String fieldName) {
        /*
        [^<] means anything but '<' - it's used instead of * so the regex will not match the whole
        kml balloon at once. we're not expecting '<' characters other than in the html tags themselves.
        the parenthesis is used to "catch" the value - this is accessed using the group(1) method.
         */
        Pattern pattern = Pattern.compile(
                "<td>[^<]*<center>[^<]*" + fieldName
                        + "[^<]*</center>[^<]*</td>[^<]*<td>[^<]*<center>([^<]*)</center>[^<]*</td>");
        Matcher matcher = pattern.matcher(mBalloonData);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}


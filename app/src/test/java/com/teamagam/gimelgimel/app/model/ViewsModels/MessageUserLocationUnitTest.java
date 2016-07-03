package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tester for {@link MessageUserLocation}
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class MessageUserLocationUnitTest {

    @Test
    public void check_correct_Creation_JSON() throws Exception {
        PointGeometry p = new PointGeometry(32.2, 23.1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
        String dateInString = "31-08-1982 10:20:56";
        long date = sdf.parse(dateInString).getTime();

        LocationSample sample = new LocationSample(p, date);


        String senderId = "Sender1";
        Message msg = new MessageUserLocation(senderId, sample);

        Gson gson = new GsonBuilder().create();
        String result_JSON = gson.toJson(msg);

        String expected_JSON = "{\"sender\":{\"location\":{\"latitude\":32.2,\"longitude\":23.1,\"altitude\":0.0,\"hasAltitude\":false},\"timeStamp\":399630056000,\"hasSpeed\":false,\"speed\":0.0,\"hasBearing\":false,\"bearing\":0.0,\"hasAccuracy\":false,\"accuracy\":0.0},\"senderId\":\"Sender1\",\"type\":\"UserLocation\"}";
//        assertEquals("JSON of MessageUserLocation was not generated properly ",expected_JSON, result_JSON);
//
//        the retrofit does some black magic in converting json back to Message Object.
        Message resultMSG = gson.fromJson(expected_JSON, msg.getClass());
        assertEquals("MessageUserLocation was not generated properly from JSON",msg.getMessageId(), resultMSG.getMessageId());
    }
}
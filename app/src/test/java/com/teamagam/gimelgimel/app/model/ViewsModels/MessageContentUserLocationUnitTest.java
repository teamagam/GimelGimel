package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yoni on 4/18/2016.
 * Tester for {@link MessageContentUserLocation}
 */
public class MessageContentUserLocationUnitTest {

    @Test
    public void check_correct_Creation_JSON() throws Exception {
        PointGeometry p = new PointGeometry(32.2, 23.1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
        String dateInString = "31-08-1982 10:20:56";
        long date = sdf.parse(dateInString).getTime();

        String userId = "User1";
        LocationSample sample = new LocationSample(p, date);


        String senderId = "Sender1";
        MessageContentUserLocation content = new MessageContentUserLocation(sample);
        Message msg = new Message<>(senderId, content, Message.USER_LOCATION);

        Gson gson = new GsonBuilder().create();
        String result_JSON = gson.toJson(msg);

        String expected_JSON = "{\"senderId\":\"Sender1\",\"type\":\"UserLocation\",\"content\":{\"locationSample\":{\"location\":{\"latitude\":32.2,\"longitude\":23.1,\"altitude\":0.0,\"hasAltitude\":false},\"timeStamp\":399630056000,\"hasSpeed\":false,\"speed\":0.0,\"hasBearing\":false,\"bearing\":0.0,\"hasAccuracy\":false,\"accuracy\":0.0}}}";
        assertEquals("JSON of MessageContentUserLocation was not generated properly ",expected_JSON, result_JSON);

        //the retrofit does some black magic in converting json back to Message Object.
//        Message resultMSG = gson.fromJson(expected_JSON, msg.getClass());
//        assertEquals("MessageContentUserLocation was not generated properly from JSON",msg, resultMSG);

    }
}
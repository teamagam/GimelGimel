package com.teamagam.gimelgimel.app.view.viewer.data;

import com.teamagam.gimelgimel.app.utils.IdCreatorUtil;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.Geometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Utility class for shared entities manipulation methods
 */
public class EntitiesHelperUtils {

    public static Point generateRandomPoint(double anchorLat, double anchorLng, int radius) {
        PointGeometry pointGeometry = EntitiesHelperUtils.generateRandomLocation(anchorLat,
                anchorLng, radius);
        PointSymbol pointSymbol = EntitiesHelperUtils.generateRandomPointSymbol();
        return new Point(IdCreatorUtil.getId(), pointGeometry, pointSymbol);
    }

    public static Polyline generateRandomPolyline(double anchorLat, double anchorLng, int radius) {
        MultiPointGeometry polylineMpg = EntitiesHelperUtils.generateRandomLocations(anchorLat,
                anchorLng, radius);
        PolylineSymbol polylineSymbol = EntitiesHelperUtils.generateRandomPolylineSymbol();
        return new Polyline(IdCreatorUtil.getId(), polylineMpg, polylineSymbol);
    }

    public static Polygon generateRandomPolygon(double anchorLat, double anchorLng, int radius) {
        MultiPointGeometry polygonMpg = EntitiesHelperUtils.generateRandomLocations(anchorLat,
                anchorLng, radius);
        PolygonSymbol polygonSymbol = EntitiesHelperUtils.generateRandomPolygonSymbol();
        return new Polygon(IdCreatorUtil.getId(), polygonMpg, polygonSymbol);
    }


    public static PolygonSymbol generateRandomPolygonSymbol() {
        return new PolygonSymbol(getRandomCssColorStirng(),
                getRandomCssColorStirng(), Math.random());
    }

    public static MultiPointGeometry generateRandomLocations(double anchorLat, double anchorLng,
                                                             int radius) {
        MultiPointGeometry mpg = new MultiPointGeometry(new ArrayList<PointGeometry>());

        for (int i = 0; i < 3; i++) {
            mpg.pointsCollection.add(generateRandomLocation(anchorLat, anchorLng, radius));
        }

        return mpg;
    }

    public static PolylineSymbol generateRandomPolylineSymbol() {
        return new PolylineSymbol(4, getRandomCssColorStirng());
    }

    public static PointGeometry generateRandomLocation(double anchorLat, double anchorLng,
                                                       double radius) {
        return new PointGeometry(anchorLat + (Math.random() * 2 * radius - radius),
                anchorLng + (Math.random() * 2 * radius - radius));
    }

    public static PointSymbol generateRandomPointSymbol() {
        String randomColor = getRandomCssColorStirng();

        PointImageSymbol pis = new PointImageSymbol("Cesium/Assets/Textures/maki/marker.png", 36,
                36);
        PointTextSymbol pts = new PointTextSymbol(randomColor, randomColor, 48);

        if (Math.random() < 0.5) {
            return pis;
        } else {
            return pts;
        }
    }

    public static String getRandomCssColorStirng() {
        String[] cssColors = new String[]{"#FF1122", "#66FF99", "#00FA12"};
        return cssColors[((int) Math.floor(Math.random() * cssColors.length))];
    }

    public static void randomlyUpdateEntities(Collection<Entity> vEntities) {
        for (Entity e : vEntities) {
            Symbol s;
            Geometry g;
            if (e instanceof Point) {
                g = EntitiesHelperUtils.generateRandomLocation(32.2, 34.8, 2);
                s = EntitiesHelperUtils.generateRandomPointSymbol();
            } else if (e instanceof Polyline) {
                g = EntitiesHelperUtils.generateRandomLocations(32.2, 34.8, 2);
                s = EntitiesHelperUtils.generateRandomPolylineSymbol();
            } else {
                //polygon
                g = EntitiesHelperUtils.generateRandomLocations(32.2, 34.8, 2);
                s = EntitiesHelperUtils.generateRandomPolygonSymbol();
            }
            e.updateSymbol(s);
            e.updateGeometry(g);
        }
    }
}

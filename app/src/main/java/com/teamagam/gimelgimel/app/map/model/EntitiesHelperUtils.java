package com.teamagam.gimelgimel.app.map.model;

import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.entities.Polygon;
import com.teamagam.gimelgimel.app.map.model.entities.Polyline;
import com.teamagam.gimelgimel.app.map.model.geometries.GeometryApp;
import com.teamagam.gimelgimel.app.map.model.geometries.MultiPointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PointSymbolApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Utility class for shared entities manipulation methods
 */
public class EntitiesHelperUtils {

    public static Point generateRandomPoint(double anchorLat, double anchorLng, int radius) {
        PointGeometryApp pointGeometry = EntitiesHelperUtils.generateRandomLocation(anchorLat,
                anchorLng, radius);
        PointSymbolApp pointSymbol = EntitiesHelperUtils.generateRandomPointSymbol();
        return new Point.Builder()
                .setGeometry(pointGeometry)
                .setSymbol(pointSymbol)
                .build();
    }

    public static Polyline generateRandomPolyline(double anchorLat, double anchorLng, int radius) {
        MultiPointGeometryApp polylineMpg = EntitiesHelperUtils.generateRandomLocations(anchorLat,
                anchorLng, radius);
        PolylineSymbol polylineSymbol = EntitiesHelperUtils.generateRandomPolylineSymbol();
        return new Polyline.Builder()
                .setGeometry(polylineMpg)
                .setSymbol(polylineSymbol)
                .build();
    }

    public static Polygon generateRandomPolygon(double anchorLat, double anchorLng, int radius) {
        MultiPointGeometryApp polygonMpg = EntitiesHelperUtils.generateRandomLocations(anchorLat,
                anchorLng, radius);
        PolygonSymbol polygonSymbol = EntitiesHelperUtils.generateRandomPolygonSymbol();
        return new Polygon.Builder()
                .setGeometry(polygonMpg)
                .setSymbol(polygonSymbol)
                .build();
    }


    public static PolygonSymbol generateRandomPolygonSymbol() {
        return new PolygonSymbol(getRandomCssColorStirng(),
                getRandomCssColorStirng(), Math.random());
    }

    public static MultiPointGeometryApp generateRandomLocations(double anchorLat, double anchorLng,
                                                                int radius) {
        MultiPointGeometryApp mpg = new MultiPointGeometryApp(new ArrayList<PointGeometryApp>());

        for (int i = 0; i < 3; i++) {
            mpg.pointsCollection.add(generateRandomLocation(anchorLat, anchorLng, radius));
        }

        return mpg;
    }

    public static PolylineSymbol generateRandomPolylineSymbol() {
        return new PolylineSymbol(4, getRandomCssColorStirng());
    }

    public static PointGeometryApp generateRandomLocation(double anchorLat, double anchorLng,
                                                          double radius) {
        return new PointGeometryApp(anchorLat + (Math.random() * 2 * radius - radius),
                anchorLng + (Math.random() * 2 * radius - radius));
    }

    public static PointSymbolApp generateRandomPointSymbol() {
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
            SymbolApp s;
            GeometryApp g;
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
//            e.updateSymbol(s);
//            e.updateGeometry(g);
        }
    }
}
